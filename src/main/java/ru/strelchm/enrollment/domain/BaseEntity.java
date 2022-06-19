package ru.strelchm.enrollment.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<T extends Serializable> {
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "created")
    private Date created;

//    @Temporal(TemporalType.TIMESTAMP)
//    @LastModifiedDate
//    @Column(name = "updated", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updated;
}
