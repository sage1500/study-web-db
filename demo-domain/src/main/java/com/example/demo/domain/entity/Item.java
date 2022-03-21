package com.example.demo.domain.entity;

import java.io.Serializable;

public class Item implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_ITEM.CODE
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    private String code;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_ITEM.NAME
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table T_ITEM
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_ITEM.CODE
     *
     * @return the value of T_ITEM.CODE
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ITEM
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public Item withCode(String code) {
        this.setCode(code);
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_ITEM.CODE
     *
     * @param code the value for T_ITEM.CODE
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_ITEM.NAME
     *
     * @return the value of T_ITEM.NAME
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ITEM
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public Item withName(String name) {
        this.setName(name);
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_ITEM.NAME
     *
     * @param name the value for T_ITEM.NAME
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ITEM
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ITEM
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Item other = (Item) that;
        return (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ITEM
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }
}