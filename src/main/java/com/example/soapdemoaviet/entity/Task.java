package com.example.soapdemoaviet.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "task")
public class Task implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Integer priority;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    public enum PriorityLevels {
        URGENT(5, "table-danger"),
        HIGH(4, "table-warning"),
        MEDIUM(3, "table-success"),
        LOW(2, "table-info"),
        NO(1, "table-secondary");

        private int code;
        private String bootstrap;

        PriorityLevels(int code, String bootstrap) {
            this.code = code;
            this.bootstrap = bootstrap;
        }

        public static PriorityLevels getCodePriority(int value) {
            for (PriorityLevels priorityLevels : PriorityLevels.values()) {
                if (priorityLevels.code == value) return priorityLevels;
            }
            throw new IllegalArgumentException("Không tồn tại value");
        }

        public int getCode() {
            return code;
        }

        public String getBootstrap() {
            return bootstrap;
        }
    }
}
