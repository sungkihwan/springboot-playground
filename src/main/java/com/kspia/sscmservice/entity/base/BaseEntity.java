package com.kspia.sscmservice.entity.base;

import com.mysql.cj.xdevapi.JsonString;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonString.class),
//        @TypeDef(name = "json", typeClass = JsonStringType.class),
//        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    // private static final long serialVersionUID = -6016373605864285893L;
}
