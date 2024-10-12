package com.cloud.gateway.common;

/**
 * Description:
 *
 * @author likain
 * @since 2024/1/2 14:08
 */
public class TokenBucket {

    private TokenBucket() {
    }

    private static final class TokenBucketHolder {
        static final TokenBucket bucket = new TokenBucket();
    }

    public static TokenBucket getInstance() {
        return TokenBucketHolder.bucket;
    }


}
