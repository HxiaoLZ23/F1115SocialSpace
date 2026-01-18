<template>
  <div class="reply-item">
    <el-avatar 
      :src="getImageUrl(reply.user?.avatar)" 
      :size="avatarSize"
      class="clickable-avatar"
      @click="$emit('go-to-profile', reply.userId)"
    >
      {{ reply.user?.nickname?.[0] || reply.user?.username?.[0] }}
    </el-avatar>
    <div class="reply-content">
      <div>
        <span class="reply-user clickable-name" @click="$emit('go-to-profile', reply.userId)">
          {{ reply.user?.nickname || reply.user?.username }}
        </span>
        <el-tag v-if="reply.user?.role === 1" type="danger" size="small" style="margin-left: 5px">管理员</el-tag>
        <el-tag v-if="reply.user?.role === 2" type="success" size="small" style="margin-left: 5px">AI</el-tag>
        <span v-if="reply.parentComment" class="reply-to">
          回复 
          <span class="clickable-name" @click="$emit('go-to-profile', reply.parentComment.userId)">
            {{ reply.parentComment.user?.nickname || reply.parentComment.user?.username }}
          </span>
        </span>
        : {{ reply.content }}
      </div>
      <div class="reply-footer">
        <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
        <el-button text size="small" @click="$emit('reply', reply)">回复</el-button>
        <el-button 
          text 
          size="small"
          :type="reply.isLiked ? 'primary' : 'default'"
          @click.stop="$emit('toggle-like', reply.id)"
        >
          <el-icon><component :is="reply.isLiked ? 'StarFilled' : 'Star'" /></el-icon>
          {{ reply.likeCount }}
        </el-button>
      </div>
    </div>
  </div>
  
  <!-- 回复输入框 -->
  <div v-if="replyingTo && replyingTo.id === reply.id" class="reply-input">
    <el-input
      :model-value="replyContent"
      @update:model-value="$emit('update:replyContent', $event)"
      type="textarea"
      :rows="2"
      :placeholder="`回复 ${reply.user?.nickname || reply.user?.username}...`"
      maxlength="500"
    />
    <div style="margin-top: 10px">
      <el-button size="small" @click="$emit('cancel-reply')">取消</el-button>
      <el-button 
        type="primary" 
        size="small" 
        @click="$emit('submit-reply')" 
        :loading="replying"
      >
        发布
      </el-button>
    </div>
  </div>
  
  <!-- 递归渲染子回复 -->
  <div v-if="reply.replies && reply.replies.length > 0" class="nested-replies">
    <reply-item
      v-for="childReply in reply.replies"
      :key="childReply.id"
      :reply="childReply"
      :avatar-size="Math.max(avatarSize - 2, 24)"
      :replying-to="replyingTo"
      :reply-content="replyContent"
      :replying="replying"
      :get-image-url="getImageUrl"
      :format-time="formatTime"
      @go-to-profile="$emit('go-to-profile', $event)"
      @reply="$emit('reply', $event)"
      @toggle-like="$emit('toggle-like', $event)"
      @cancel-reply="$emit('cancel-reply')"
      @submit-reply="$emit('submit-reply')"
      @update:replyContent="$emit('update:replyContent', $event)"
    />
  </div>
</template>

<script>
export default {
  name: 'ReplyItem'
}
</script>

<script setup>
defineProps({
  reply: {
    type: Object,
    required: true
  },
  avatarSize: {
    type: Number,
    default: 30
  },
  replyingTo: {
    type: Object,
    default: null
  },
  replyContent: {
    type: String,
    default: ''
  },
  replying: {
    type: Boolean,
    default: false
  },
  getImageUrl: {
    type: Function,
    required: true
  },
  formatTime: {
    type: Function,
    required: true
  }
})

defineEmits([
  'go-to-profile',
  'reply',
  'toggle-like',
  'cancel-reply',
  'submit-reply',
  'update:replyContent'
])
</script>

<style scoped>
.reply-item {
  display: flex;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #e4e7ed;
}

.reply-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.reply-content {
  flex: 1;
}

.reply-user {
  font-weight: 500;
  color: #303133;
}

.reply-to {
  color: #909399;
  margin: 0 5px;
}

.reply-footer {
  margin-top: 5px;
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 15px;
}

.reply-time {
  color: #909399;
}

.reply-input {
  margin: 15px 0 0 46px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.nested-replies {
  margin: 10px 0 0 40px;
  padding: 10px;
  background-color: #fafbfc;
  border-left: 2px solid #e4e7ed;
  border-radius: 4px;
}

.nested-replies .reply-input {
  margin: 10px 0 0 0;
  padding: 10px;
  background-color: #ffffff;
  border: 1px solid #e4e7ed;
}

.clickable-avatar,
.clickable-name {
  cursor: pointer;
}

.clickable-name:hover {
  color: #409eff;
}
</style>
