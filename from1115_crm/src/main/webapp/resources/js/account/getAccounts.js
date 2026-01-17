
let user = JSON.parse(window.localStorage.getItem("user"))
var isAdmin = user.role == 1


new Vue({
    el: "#app",
    data: {
        /* 账户列表 */
        accountList: [],
        /* 页码 */
        pageNum: 1,
        /* 每页显示的条数 */
        pageSize: 10,
        /* 总记录数 */
        total: 0,
        /* 可选择的每页显示的条数 */
        pageSizes: [2, 5, 10],
        /* 是否显示添加账户弹窗 */
        addAccountStatus: false,
        /* 添加账户的数据绑定 */
        addAccountFormData: {
            username: undefined
        },

        /* 判断当前用户是否是超级管理员 */
        isAdmin:isAdmin,
        /* 前端数据校验 */
        formRules: {
            username: [
                {required: true, message: "请输入账户名称", trigger: "blur"}
            ],
        }
    },
    /* 页面加载完成执行的函数 */
    created: function () {
        this.getAccountByPage()
    },
    methods: {
        /* 获取账户列表 */
        getAccountByPage: function () {
            axios({
                url: "/account/getAccountsByPage.do",
                method: "GET",
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize
                }
            }).then((result) => {
                let code = result.data.code
                if (code == 200) {
                    this.accountList = result.data.data
                    this.total = result.data.total
                } else {
                    this.$message({
                        showClose: true,
                        message: "服务器忙",
                        type: "error"
                    })
                }
            })
        },
        /* 页码发生变化时触发 */
        currentChange: function (newPageNum) {
            this.pageNum = newPageNum
            this.getAccountByPage()
        },
        /* 每页显示的条数发生变化时触发 */
        sizeChange: function (newPageSize) {
            this.pageSize = newPageSize
            this.getAccountByPage()
        },
        /* 对账户头像进行格式化 */
        imgUrlFmt: function (row) {
            return row.imgUrl == null || row.imgUrl == '' ? '暂无头像' : row.imgUrl
        },

        /* 对修改时间进行格式化 */
        updateTimeFmt: function (row) {
            return row.updateTime == null || row.updateTime == '' ? '暂无时间' : row.updateTime
        },

        /* 添加账户的按钮单击时间 */
        addAccountWin: function () {
            this.addAccountStatus = !this.addAccountStatus
        },
        /* 关闭添加账户窗口时清空数据 */
        addAccountClose: function () {
            this.$refs['addAccountFef'].resetFields()
        },

        /* 清空添加账户窗口的数据 */
        addAccountCancel: function () {
            this.$refs['addAccountFef'].resetFields()
        },

        /* 添加账户确认按钮 */
        addAccountOk: function () {
            console.log("data:", this.addAccountFormData)
            this.$refs['addAccountFef'].validate((valid) => {
                if (valid) {
                    // 通过了数据校验
                    axios({
                        url: "/account/addAccount.do",
                        method: "PUT",
                        params: this.addAccountFormData
                    }).then((result) => {
                        let code = result.data.code
                        if (code == 200) {
                            // 添加成功
                            // 1. 关闭 添加弹窗
                            this.addAccountStatus = false
                            this.getAccountByPage()
                            this.total = result.data.total
                        } else {
                            // 添加失败
                            this.$message({
                                showClose: true,
                                message: result.data.msg,
                                type: "error"
                            })
                        }
                    })
                }
            })
        },

        /* 根据ID删除账户 */
        delOneAccount:function (row) {
            let id = row.id
            this.$confirm("你确定要删除当前账户吗?","温馨提示").then(()=>{
                axios({
                    url:"/account/cutOneAccount.do",
                    method:"DELETE",
                    params:{
                        id:id
                    }
                }).then((result)=>{
                    let code = result.data.code
                    if (code == 200) {
                        // 删除成功
                        this.$message({
                            showClose: true,
                            message: "删除成功",
                            type: "success"
                        })
                        // 更新当前账户列表
                        this.getAccountByPage()
                    } else {
                        // 删除失败
                        this.$message({
                            showClose: true,
                            message: result.data.msg,
                            type: "error"
                        })
                    }
                })
            })
        },

        /* 根据ID重置账户的密码 */
        restAccountPwd:function (row) {
            let id = row.id
            this.$confirm("您确定要重置当前账户的密码吗?","温馨提示").then(()=>{
                axios({
                    url:"/account/editResetAccountPwd.do",
                    method:"POST",
                    params:{
                        id:id
                    }
                }).then((result)=>{
                    let code = result.data.code
                    if (code == 200) {
                        // 重置密码成功
                        this.$message({
                            showClose: true,
                            message: "重置密码成功",
                            type: "success"
                        })
                        // 如果是超级管理员(自己重置了自己的密码) 需要退出登录
                        if (row.role==1) {
                            window.location.href="../../../views/login.html"
                        }
                    } else {
                        // 重置密码失败
                        this.$message({
                            showClose: true,
                            message: result.data.msg,
                            type: "error"
                        })
                    }
                })
            })
        }

    }
})