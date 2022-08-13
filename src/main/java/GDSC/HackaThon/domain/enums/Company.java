package GDSC.HackaThon.domain.enums;

import lombok.Getter;

/**
 * 자전거 만든 회사
 */
@Getter
public enum Company {

    SAMSUNG("삼성"),
    LG("엘지"),
    SK("스카이웨어");

    private String name;

    private Company(String name) {
        this.name = name;
    }

}
