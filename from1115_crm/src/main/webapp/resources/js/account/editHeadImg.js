new Vue({
    el:"#app",
    data:{
        imgUrl:undefined
    },

    methods:{
        onChange:function (file) {
            let flag = file.size/1024/1024>1
            console.log(flag)
            if (flag) {
                this.$message({
                    showClose: true,
                    message: "对不起，图片大小不能超过1M",
                    type: "error"
                })
                return false
            }
            this.imgUrl = URL.createObjectURL(file.raw)
        },

        /* 头像上传成功后执行的函数 */
        onSuccess:function (result) {
            console.log(result)
            let code = result.code
            if (code == 200) {
                this.$message({
                    showClose:true,
                    message: "上传成功",
                    type: "success"
                })
                this.imgUrl = result.data
                window.parent.document.getElementById("headImg").src="/download/downloadFile.do?imgUrl="+this.imgUrl
            } else {
                this.$message({
                    showClose:true,
                    message: "头像上传失败",
                    type: "error"
                })
            }
        },

        /* 上传头像 */
        uploadHeadImg:function () {
            this.$refs['uploadHeadImgRef'].submit();
        }
    }
})