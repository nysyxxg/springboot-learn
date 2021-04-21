package com.example.one.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tbl_order
 * @author 
 */
@Data
public class TblOrder implements Serializable {
    private Integer orderId;

    private String buyer;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}