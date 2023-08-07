package com.sky.vo;

import com.sky.entity.Employee;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xujj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页查询后的数据封装")
public class EmployeePageQueryVO {
    private Integer total;
    private List<Employee> records;
}
