new Vue({
    el:"#app",
    data:{
        /* 用户列表 */
        userList:[],
        /* 页码 */
        pageNum: 1,
        /* 每页显示的条数 */
        pageSize:10,
        /* 总记录数 */
        total:0,
        /* 可修改的每页显示条数 */
        pageSizes:[3,5,10],
        /* 动态搜索的数据绑定 */
        searchFormData:{
            username:undefined,
            tel:undefined,
            deptName:undefined,
            gender:undefined
        },
        /* 添加弹出框的是否显示 */
        addUserStatus:false,

        /* 编辑弹出框的是否显示 */
        editUserStatus:false,

        /* 添加客户信息数据 */
        addUserFormData: {
            username:undefined,
            birthday:undefined,
            gender:undefined,
            tel:undefined,
            sal:undefined,
            profession:undefined,
            address:undefined,
            remark:undefined,
            deptId:undefined
        },
        /* 编辑客户信息数据 */
        editUserFormData:{
            id:undefined,
            username:undefined,
            birthday:undefined,
            gender:undefined,
            tel:undefined,
            sal:undefined,
            profession:undefined,
            address:undefined,
            remark:undefined,
            deptId:undefined
        },
        /* 批量删除，选中的ID */
        delUserArray:[],
        /* 添加客户信息数据校验 */
        formRules:{
            username:[
                {required:true, message: "请输入客户名称",trigger:"blur"},
                {min:2,max:10,message: "客户名称的长度是2-10之间",trigger:"blur"}
            ],
            birthday:[
                {required:true, message: "请选择生日日期",trigger:"blur"}

            ],
            gender:[
                {required:true, message: "请选择客户性别",trigger:"blur"}

            ],
            tel:[
                {required:true, message: "请请输入手机号",trigger:"blur"},
                {min:11,max:11,message: "手机号码只能是11位",trigger:"blur"}
            ],
            sal:[
                {required:true, message: "请输入薪资",trigger:"blur"}
            ],
            profession:[
                {required:true, message: "请选择职业",trigger:"blur"}
            ],
            address:[
                {required:true, message: "请输入地址",trigger:"blur"},
                {min:5,max:100,message: "地址的长度只能是5-100之间",trigger:"blur"}
            ],
            deptId:[
                {required:true, message: "请选择部门ID",trigger:"blur"}
            ],
        }

    },
    created:function () {
       this.getUserByPage()
    },
    methods: {
        /* 分页查询客户列表 */
        getUserByPage:function () {
            axios({
                url:"/user/getUsersByPage.do",
                method:"GET",
                params:{
                    pageNum:this.pageNum,
                    pageSize:this.pageSize
                }
            }).then((result)=>{
                this.userList = result.data.data
                this.total = result.data.total
            })
        },

        /* 格式化性别内容 */
        genderFmt:function (row) {
            return row.gender == 1 ? '男' : '女'
        },

        /* 格式化职业内容 */
        professionFmt:function (row) {
            let profession = ""
            switch (row.profession) {
                case "1":
                    profession = "董事长"
                    break;
                case "2":
                    profession = "程序员"
                    break;
                case "3":
                    profession = "产品经理"
                    break;
                case "4":
                    profession = "销售员"
                    break;
            }
            return profession;
        },
        /* 格式化薪资 */
        salFmt:function (row) {
            return '¥'+row.sal;
        },
        /* 页码发生变化时触发 */
        currentChange:function (newPageNum) {
            this.pageNum = newPageNum
            this.getUserByPage()
        },
        /* 每页显示的条数发生变化时触发 */
        sizeChange:function (newPageSize) {
            this.pageSize = newPageSize

        },
        /* 动态搜索客户信息 */
        searchUsers:function () {
            axios({
                url:"/user/getUsersBySearch.do",
                method:"GET",
                params:this.searchFormData
            }).then((result=>{
                let code = result.data.code
                if (code == 200) {
                    // 搜索成功
                    this.userList = result.data.data
                    this.total = result.data.total
                    this.pageNum = 1
                    this.pageSize = 10
                } else {
                    this.$message({
                        showClose: true,
                        message: result.data.msg+'搜索失败',
                        type: 'error'
                    });
                }
            }))
        },
        /* 添加用户弹出框 */
        addUsersWin:function () {
            this.addUserStatus = !this.addUserStatus
        },

        /* 添加客户信息确认按钮 */
        addUserOk:function () {
            console.log("data:",this.addUserFormData)
            this.$refs['addUserFef'].validate((valid)=>{
                if (valid) {
                    // 前段数据通过了校验，可以向后台请求接口
                    axios({
                        url:"/user/addUser.do",
                        method:"POST",
                        params:this.addUserFormData

                    }).then((result)=>{
                        let code = result.data.code
                        if(code == 200) {
                            // 添加成功
                            // 关闭添加窗口
                            this.addUserStatus = false
                            // 刷新客户列表页面
                            this.getUserByPage()
                            this.total = result.data.total
                            // 给用户提示添加成功
                            this.$message({
                                showClose: true,
                                message: '添加客户成功',
                                type: 'success'
                            });
                        } else {
                            // 添加失败 提示添加失败消息
                            this.$message({
                                showClose: true,
                                message: result.data.msg+'添加客户失败',
                                type: 'error'
                            });
                        }
                    })
                }
            })
        },

        /* 编辑客户 */
        editUserOk:function () {
            this.$refs['editUserFef'].validate((valid)=>{
                if (valid) {
                    // 参数没有问题，可以请求后台
                    axios({
                        url:"/user/editUser.do",
                        method:"PUT",
                        params:this.editUserFormData
                    }).then((result)=>{
                        let code = result.data.code
                        if (code == 200) {
                            // 编辑成功
                            this.$message({
                                showClose:true,
                                message:"编辑成功",
                                type:"success"
                            })
                            this.editUserStatus = !this.editUserStatus
                            this.getUserByPage()
                        } else {
                            // 编辑失败
                            this.$message({
                                showClose:true,
                                message:"编辑失败"+result.data.msg,
                                type:"error"
                            })
                        }
                    })
                } else {
                    // 编辑的参数有误
                    this.$message({
                        showClose:true,
                        message:"编辑的信息有误",
                        type:"error"
                    })
                }
            })
        },

        /* 关闭添加客户信息框时触发的函数 */
        addUserClose:function () {
            console.log("弹框被关闭了")
            this.$refs['addUserFef'].resetFields()
        },

        /* 关闭编辑客户信息框时触发的函数 */
        editUserClose:function () {
            this.$refs['editUserFef'].resetFields()
        },

        /* 清除添加框的客户信息 */
        addUserCancel:function () {
            this.$refs['addUserFef'].resetFields()
        },
        /* 清除编辑框的客户信息 */
        editUserCancel:function () {
            //this.$refs['editUserFef'].resetFields()
            this.editUserFormData.username = undefined
            this.editUserFormData.tel = undefined
        },

        /* 批量删除 全选 */
        selectAll:function (params) {
            this.delUserArray = []
            params.filter((item)=>{
                this.delUserArray.push(item.id)
            })
            console.log(this.delUserArray)
        },

        /* 批量删除 一个一个的选中 */
        selectOne:function (params) {
            this.delUserArray = []
            params.filter((item)=>{
                this.delUserArray.push(item.id)
            })
            console.log(this.delUserArray)
        },
        /* 实现批量删除按钮 */
        delManyUser:function () {
            let len = this.delUserArray.length
            if (len == 0) {
                // 没有选择任何客户，不能删除并且要提示
                this.$alert("请至少选择一个客户信息","温馨提示")
            } else {
                this.$confirm("您确定要删除这些客户信息吗?","温馨提示").then(()=>{
                    //删除客户
                    axios({
                        url:"/user/cutManyUser.do",
                        method:"DELETE",
                        params:{
                            id:this.delUserArray.join(",")
                        }
                    }).then((result)=>{
                        let code = result.data.code
                        if (code == 200) {
                            // 删除成功
                            this.$message({
                                showClose:true,
                                message:"删除成功",
                                type:"success"
                            })
                            this.getUserByPage()
                        } else {
                            // 删除失败
                            this.$message({
                                showClose:true,
                                message:"删除失败"+result.data.msg,
                                type:"error"
                            })
                        }
                    })
                }).catch(()=>{

                })
            }
        },

        /* 根据id删除单个客户信息 */
        delOneUser:function (row) {
            let id = row.id
            this.$confirm("您确认删除当前客户吗?","温馨提示").then(()=>{
                axios({
                    url:"/user/cutOneUser.do",
                    method:"DELETE",
                    params:{
                        id:id
                    }
                }).then((result)=>{
                    let code = result.data.code
                    if (code == 200) {
                        // 删除成功
                        this.$message({
                            showClose:true,
                            message:"删除成功",
                            type:"success"
                        })
                        this.getUserByPage()
                    } else {
                        // 删除失败
                        this.$message({
                            showClose:true,
                            message:"删除失败"+result.data.msg,
                            type:"error"
                        })
                    }
                })
            }).catch(()=>{

            })
        },
        /* 编辑客户信息 */
        editUser:function (row) {
            console.log('data',row)
            /* 弹出编辑框 */
            this.editUserStatus = !this.editUserStatus
            /* 实现数据的回显 */
            this.editUserFormData = row
            this.editUserFormData.gender = Number(row.gender)
        }

    }
})