package org.clever.security.embed.authorization.voter;

import lombok.Getter;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 18:09 <br/>
 */
@Getter
public enum VoterResult {
    /**
     * 通过
     */
    PASS(1),
    /**
     * 驳回
     */
    REJECT(-1),
    /**
     * 弃权
     */
    ABSTAIN(0);

    private final int id;

    VoterResult(int id) {
        this.id = id;
    }
}
