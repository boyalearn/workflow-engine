package com.workflow.listener;

/**
 * 1.监听器从使用范围上可以分为：执行监听器和任务监听器。
 * 2.从功能上又分为用户自定义监听器以及系统内置记录监听器（主要操作历史表）。
 * 3.任务监听器支持的事件类型有如下四钟：
 *       节点分配处理人（“assignment”）、
 *       创建节点（“create”）、
 *       任务完成（“complete”）、
 *       任务删除（“delete”）
 * 4.执行监听器支持的是事件类型有如下三种
 *       开始（“start”）
 *       结束（“end”）
 *       途径连线（“take”） 仅支持在连线中进行配置和使用。
 *
 * 5.任务监听器与执行监听器仅仅是实现接口类不同而已，
 *       任务监听器需要实现TaskListener接口
 *       执行监听器需要实现ExecutionListener接口
 *
 */
public class MasterListener {
}
