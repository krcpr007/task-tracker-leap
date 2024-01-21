import React, { Component } from "react";
import "./Task.css";
import { Checkbox, Row, Col, notification ,Modal,Form, DatePicker ,Input} from "antd";
import { completeTask  ,deleteTask , updateTask} from "../util/APIUtils";
import { FiEdit3 } from "react-icons/fi";
import { MdDeleteSweep } from "react-icons/md";
import moment from "moment";
class Task extends Component {
  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      checked: this.props.task.complete,
    };
    this.handleChange = this.handleChange.bind(this);
  }

    handleChange(id, event) {
      let status = !this.state.checked;
      let completeRequest = {
        taskId: id,
        status: status,
      };
      let notifDescription = "";
      if (status) {
        notifDescription = "Task marked as COMPLETED";
      } else {
        notifDescription = "Task marked as INCOMPLETE";
      }

    completeTask(completeRequest)
      .then((response) => {
        this.setState({ checked: status });
        notification.success({
          message: "SUCCESS",
          description: notifDescription,
        });
      })
      .catch((error) => {
        if (error.status === 401) {
          notification.error({
            message: "ERROR",
            description: error.message || "Authentication Error.",
          });
        } else {
          notification.error({
            message: "ERROR",
            description:
              error.message || "Sorry! Something went wrong. Please try again!",
          });
        }
      });
    }

    handleDeleteTask = (id) => {
        deleteTask(this.props.task.id)
        .then(response => {
            notification.success({
                message: 'SUCCESS',
                description: 'Successfully deleted task.'
            }); 
          //  getTasks(); 
          // @important
          window.location.reload() // i know this not a good practice to refresh the page but i am not able to update the task list after deleting the task
        }).catch(error => {
            if(error.status === 401) {
                notification.error({
                    message: 'ERROR',
                    description: error.message || 'Authentication Error.'
                });   
            } else {
                notification.error({
                    message: 'ERROR',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });        
            }
        });
    }
    saveFormRef = (formRef) => {
        this.formRef = formRef;
    }
    handleModal = () => {
        this.setState({
            visible: this.state.visible?false:true,
        });
    }
    handleCancel = (e) => {
        this.setState({
          visible: false,
        });
        
    }

    handleUpdateTask = () => {
      console.log("handleUpdateTask")
        const form = this.formRef.props.form;
        form.validateFields((err, values) => {
          if (err) {
            return;
          }

          const task = {
              "id": this.props.task.id,
              "name": values['name'],
              "date": values['date-picker'].format('YYYY-MM-DD'),
              "description": values['description']
          }
          // this is for creating the task but i want ki update it
          updateTask(task)
            .then(response => {
                notification.success({
                    message: 'SUCCESS',
                    description: 'Successfully updated task.'
                }); 
              window.location.reload() // i know this not a good practice to refresh the page but i am not able to update the task list after updating the task
            }).catch(error => {
                if(error.status === 401) {
                    notification.error({
                        message: 'ERROR',
                        description: error.message || 'Authentication Error.'
                    });   
                } else {
                    notification.error({
                        message: 'ERROR',
                        description: error.message || 'Sorry! Something went wrong. Please try again!'
                    });        
                }
            });
           

          form.resetFields();
          this.setState({ visible: false });
        });
    }
    
  render() {
    return (
        <>
      <Row className="task">
        <Col span={4}>
          <Checkbox
            className="complete-check"
            onChange={this.handleChange.bind(this, this.props.task.id)}
            checked={this.state.checked}
          ></Checkbox>
        </Col>
        <Col span={20} className="task-content">
          <div className="task-name" style={this.state.checked?{textDecoration:"line-through"}:null}>{this.props.task.name}</div>
          <div className="task-desc">{this.props.task.description}</div>
          <div className="task-date">{this.props.task.date}</div>
          <div className="btn">
            <button className='btn-green' onClick={this.handleModal}><FiEdit3 /></button>
            <button className='btn-red' onClick={this.handleDeleteTask.bind(this.props.task.id)}><MdDeleteSweep/></button>
        </div>
        </Col>
        <TaskEditForm  wrappedComponentRef={this.saveFormRef}
                    visible={this.state.visible}
                    onCancel={this.handleCancel}
                    onCreate={this.handleUpdateTask}
                    title={this.props.task.name}
                    description={this.props.task.description}
                    date={this.props.task.date}
                    />
      </Row>
    </>
    );
  }
}

export default Task;
const TaskEditForm = Form.create({ name: 'create_task' })(
    class EditForm extends Component {
        render() {
            const { getFieldDecorator } = this.props.form;
            const {
                visible, onCreate,
                title,description,date ,onCancel
            } = this.props;

            return (
                <Modal
                    visible={visible}
                    title="Create new Task"
                    okText="Create"
                    onCancel={onCancel}
                    onOk={onCreate}
                >
                    <Form layout="vertical">
                        <Form.Item label="Task Title">
                            {getFieldDecorator('name', {
                               initialValue: title,
                                rules: [{ required: true, message: 'Please enter the task title', whitespace: true }],
                            })(
                                <Input />
                            )}
                        </Form.Item>
                        <Form.Item label="Task Description">
                            {getFieldDecorator('description', {
                               initialValue: description,
                                rules: [{ required: false, message: 'Please enter the description', whitespace: true }],
                            })(
                                <Input />
                            )}
                        </Form.Item>
                        <Form.Item label="Date">
                                {getFieldDecorator('date-picker', {
                                    initialValue: moment(date),
                                    rules: [{ type: 'object', required: true, message: 'Please select time!' }]
                                })(
                                    <DatePicker format={"YYYY-MM-DD"}/>
                                )}
                        </Form.Item>
                    </Form>
                </Modal>
            )
        }
    }
)