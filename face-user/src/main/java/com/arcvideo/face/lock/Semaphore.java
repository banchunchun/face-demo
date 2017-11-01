package com.arcvideo.face.lock;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:16:22
 */
public class Semaphore {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String prefix = "semaphore/";  // 信号量参数前缀

    private ConsulClient consulClient;
    private int limit;
    private String keyPath;
    private String sessionId = null;
    private boolean acquired = false;

    /**
     * @param consulClient consul客户端实例
     * @param limit        信号量上限值
     * @param keyPath      信号量在consul中存储的参数路径
     */
    public Semaphore(ConsulClient consulClient, int limit, String keyPath) {
        this.consulClient = consulClient;
        this.limit = limit;
        this.keyPath = prefix + keyPath;
    }

    /**
     * acquired信号量
     *
     * @param block 是否阻塞。如果为true，那么一直尝试，直到获取到该资源为止。
     * @return
     * @throws IOException
     */
    public Boolean acquired(boolean block) throws IOException {

        if (acquired) {
            logger.error(sessionId + " - Already acquired");
            throw new RuntimeException(sessionId + " - Already acquired");
        }

        // create session
        clearSession();
        this.sessionId = createSessionId("semaphore");
        logger.debug("Create session : " + sessionId);

        // add contender entry
        String contenderKey = keyPath + "/" + sessionId;
        logger.debug("contenderKey : " + contenderKey);
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);
        Boolean b = consulClient.setKVValue(contenderKey, "", putParams).getValue();
        if (!b) {
            logger.error("Failed to add contender entry : " + contenderKey + ", " + sessionId);
            throw new RuntimeException("Failed to add contender entry : " + contenderKey + ", " + sessionId);
        }

        while (true) {
            // try to take the semaphore
            String lockKey = keyPath + "/.lock";
            String lockKeyValue;

            GetValue lockKeyContent = consulClient.getKVValue(lockKey).getValue();

            if (lockKeyContent != null) {
                // lock值转换
                lockKeyValue = lockKeyContent.getValue();
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] v = decoder.decodeBuffer(lockKeyValue);
                String lockKeyValueDecode = new String(v);
                logger.debug("lockKey=" + lockKey + ", lockKeyValueDecode=" + lockKeyValueDecode);

                Gson gson = new Gson();
                ContenderValue contenderValue = gson.fromJson(lockKeyValueDecode, ContenderValue.class);
                // 当前信号量已满
                if (contenderValue.getLimit() == contenderValue.getHolders().size()) {
                    logger.debug("Semaphore limited " + contenderValue.getLimit() + ", waiting...");
                    if (block) {
                        // 如果是阻塞模式，再尝试
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                        }
                        continue;
                    }
                    // 非阻塞模式，直接返回没有获取到信号量
                    return false;
                }
                // 信号量增加
                contenderValue.getHolders().add(sessionId);
                putParams = new PutParams();
                putParams.setCas(lockKeyContent.getModifyIndex());
                boolean c = consulClient.setKVValue(lockKey, contenderValue.toString(), putParams).getValue();
                if (c) {
                    acquired = true;
                    return true;
                } else
                    continue;
            } else {
                // 当前信号量还没有，所以创建一个，并马上抢占一个资源
                ContenderValue contenderValue = new ContenderValue();
                contenderValue.setLimit(limit);
                contenderValue.getHolders().add(sessionId);

                putParams = new PutParams();
                putParams.setCas(0L);
                boolean c = consulClient.setKVValue(lockKey, contenderValue.toString(), putParams).getValue();
                if (c) {
                    acquired = true;
                    return true;
                }
                continue;
            }
        }
    }

    /**
     * 创建sessionId
     *
     * @param sessionName
     * @return
     */
    public String createSessionId(String sessionName) {
        NewSession newSession = new NewSession();
        newSession.setName(sessionName);
        return consulClient.sessionCreate(newSession, null).getValue();
    }

    /**
     * 释放session、并从lock中移除当前的sessionId
     *
     * @throws IOException
     */
    public void release() throws IOException {
        if (this.acquired) {
            // remove session from lock
            while (true) {
                String contenderKey = keyPath + "/" + sessionId;
                String lockKey = keyPath + "/.lock";
                String lockKeyValue;

                GetValue lockKeyContent = consulClient.getKVValue(lockKey).getValue();
                if (lockKeyContent != null) {
                    // lock值转换
                    lockKeyValue = lockKeyContent.getValue();
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] v = decoder.decodeBuffer(lockKeyValue);
                    String lockKeyValueDecode = new String(v);
                    Gson gson = new Gson();
                    ContenderValue contenderValue = gson.fromJson(lockKeyValueDecode, ContenderValue.class);
                    contenderValue.getHolders().remove(sessionId);
                    PutParams putParams = new PutParams();
                    putParams.setCas(lockKeyContent.getModifyIndex());
                    consulClient.deleteKVValue(contenderKey);
                    boolean c = consulClient.setKVValue(lockKey, contenderValue.toString(), putParams).getValue();
                    if (c) {
                        break;
                    }
                }
            }
            // remove session key

        }
        this.acquired = false;
        clearSession();
    }

    public void clearSession() {
        if (sessionId != null) {
            consulClient.sessionDestroy(sessionId, null);
            sessionId = null;
        }
    }

    class ContenderValue implements Serializable {

        private Integer limit;
        private List<String> holders = Lists.newArrayList();

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public List<String> getHolders() {
            return holders;
        }

        public void setHolders(List<String> holders) {
            this.holders = holders;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

    }
}
