package com.example.demo.domain.simple.repository2;

import com.example.demo.domain.entity.Order;
import com.example.demo.domain.entity.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderRepository2 {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    long countByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int deleteByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int insert(Order row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int insertSelective(Order row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    List<Order> selectByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    Order selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int updateByExampleSelective(@Param("row") Order row, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int updateByExample(@Param("row") Order row, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int updateByPrimaryKeySelective(Order row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_order
     *
     * @mbg.generated Thu Mar 24 02:05:29 JST 2022
     */
    int updateByPrimaryKey(Order row);
}