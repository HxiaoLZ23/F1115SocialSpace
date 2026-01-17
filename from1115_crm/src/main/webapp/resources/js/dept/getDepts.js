new Vue({
    el:"#app",
    data:{
        /* 部门列表 */
        deptList:[],
        /* 页码 */
        pageNum: 1,
        /* 每页显示的条数 */
        pageSize:10,
        /* 总记录数 */
        total:0,
        /* 可修改的每页显示条数 */
        pageSizes:[2,5,10],
        /* 是否显示添加部门弹窗 */
        addDeptStatus: false,
        /* 添加部门的数据绑定 */
        addDeptFormData: {
            name: undefined,
            loc:undefined

        },
    },
    created:function () {
        this.getDeptByPage()
    },
    methods:{
        /* 分页查询客户列表 */
        getDeptByPage:function () {
            axios({
                url:"/dept/getDeptsByPage.do",
                method:"GET",
                params:{
                    pageNum:this.pageNum,
                    pageSize:this.pageSize
                }
            }).then((result)=>{
                this.deptList = result.data.data
                this.total = result.data.total
                console.log(result)
            })
        },

        /* 对创建时间进行格式化 */
        createTimeFmt: function (row) {
            return row.createTime == null || row.createTime == '' ? '暂无时间' : row.createTime
        },

        /* 对修改时间进行格式化 */
        updateTimeFmt: function (row) {
            return row.updateTime == null || row.updateTime == '' ? '暂无时间' : row.updateTime
        },

        /* 页码发生变化时触发 */
        currentChange: function (newPageNum) {
            this.pageNum = newPageNum
            this.getDeptByPage()
        },
        /* 每页显示的条数发生变化时触发 */
        sizeChange: function (newPageSize) {
            this.pageSize = newPageSize
            this.getDeptByPage()

        },

        /* 关闭添加账户窗口时清空数据 */
        addDeptClose: function () {
            this.$refs['addDeptRef'].resetFields()
        },

        /* 添加账户的按钮单击时间 */
        addDeptWin: function () {
            this.addDeptStatus = !this.addDeptStatus
        },

        /* 前端数据校验 */
        formRules: {
            name: [
                {required: true, message: "请输入部门名称", trigger: "blur"}
            ],
            loc: [
                {required: true, message: "请输入部门地址", trigger: "blur"}
            ],
        },

        /* 清空添加部门窗口的数据 */
        addDeptCancel: function () {
            this.$refs['addDeptRef'].resetFields()
        },

        /* 添加部门确认按钮 */
        addDeptOk: function () {
            console.log("data:", this.addDeptFormData)
            this.$refs['addDeptRef'].validate((valid) => {
                if (valid) {
                    // 通过了数据校验
                    axios({
                        url: "/dept/addDept.do",
                        method: "POST",
                        params: this.addDeptFormData
                    }).then((result) => {
                        let code = result.data.code
                        if (code == 200) {
                            // 添加成功
                            // 1. 关闭 添加弹窗
                            this.addDeptStatus = false
                            this.getDeptByPage()
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
    }
})