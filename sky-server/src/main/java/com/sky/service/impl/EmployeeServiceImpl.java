package com.sky.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!employee.getPassword().equals(md5Password)) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3、返回实体对象
        return employee;
    }

    /**
     * @Description :添加员工
     */

    @Override
    public void insert(EmployeeDTO employeeDto) {

        // 设置属性
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDto, employee);
        employee.setStatus(StatusConstant.ENABLE);
        // TODO:设置当前登陆员工的ID---jwt令牌解析获取id
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 插入数据库
        employeeMapper.insertEmployee(employee);

    }


    @Override
    public PageResult getEmployees(EmployeePageQueryDTO employeePageQueryDTO) {
        // TODO 调用Mapper利用分页插件获取员工信息
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        List<Employee> lists = employeeMapper.selectEmployee(employeePageQueryDTO.getName());
        Page<Employee> pages = (Page<Employee>)lists;
        return new PageResult(pages.getTotal(),pages.getResult());

    }

}
