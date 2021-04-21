package com.example.two.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tbl_inventory
 * @author 
 */
@Data
public class TblInventory implements Serializable {
    private Integer goodId;

    private Integer num;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}