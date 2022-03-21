package com.example.demo.domain.entity;

import java.io.Serializable;

public class Order implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_ORDER.ID
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_ORDER.STATUS_CODE
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    private Integer statusCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table T_ORDER
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_ORDER.ID
     *
     * @return the value of T_ORDER.ID
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ORDER
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public Order withId(Long id) {
        this.setId(id);
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_ORDER.ID
     *
     * @param id the value for T_ORDER.ID
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_ORDER.STATUS_CODE
     *
     * @return the value of T_ORDER.STATUS_CODE
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ORDER
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public Order withStatusCode(Integer statusCode) {
        this.setStatusCode(statusCode);
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_ORDER.STATUS_CODE
     *
     * @param statusCode the value for T_ORDER.STATUS_CODE
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ORDER
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", statusCode=").append(statusCode);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ORDER
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
        Order other = (Order) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStatusCode() == null ? other.getStatusCode() == null : this.getStatusCode().equals(other.getStatusCode()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ORDER
     *
     * @mbg.generated Mon Mar 21 01:27:40 JST 2022
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStatusCode() == null) ? 0 : getStatusCode().hashCode());
        return result;
    }
}