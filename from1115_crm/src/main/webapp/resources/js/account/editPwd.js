let userObj = JSON.parse(window.localStorage.getItem("user"))
let uName = userObj.username

new Vue({
    el:"#app",
    data:{
        fromData:{
            username:uName,
            pwd:undefined
        },
        /* 校验前端输入框(密码) */
        fromRules:{
            pwd:[
                {required:true, message:"请输入密码",trigger:"blur"},
                {min:6,max:6,message: "密码的长度只能是6位",trigger: "blur"}
            ]
        }
    },
    methods:{
        /* 重置表单 */
        resetForm:function () {
            this.$refs['formDataRef'].resetFields()
        },
        /* 修改密码 */
        editPwdOk:function () {
            this.$refs['formDataRef'].validate((valid)=>{
                if (valid) {
                    // 数据校验成功，可以请求后台了
                    this.$confirm("您是否确定修改当前账户的密码","温馨提示").then(()=>{
                        axios({
                            url:"/account/editAccountPwd.do",
                            method:"POST",
                            params:{
                                pwd:this.fromData.pwd
                            }
                        }).then((result)=>{
                            let code = result.data.code
                            if (code == 200) {
                                this.$message({
                                    showClose:true,
                                    message:"修改成功",
                                    type:"success"
                                })
                                // 页面的跳转 跳转到登录页面
                                window.location.href="../../../views/login.html"
                            } else {
                                this.$message({
                                    showClose:true,
                                    message:result.data.msg,
                                    type:"error"
                                })
                            }

                        })
                    })
                }
            })
        }
    }

})