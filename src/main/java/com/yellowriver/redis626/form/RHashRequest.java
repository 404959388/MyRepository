package com.yellowriver.redis626.form;

import lombok.*;

/**
 * @author huanghe
 * @create 2022/2/18 3:36 PM
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RHashRequest<T> {
    /**
     * mapName
     */
    private String name;
    private String key;
    private T body;
    /**
     * 单位秒,-1表示永久,如需设置需要 > 0
     */
    private int expiry = -1;

}
