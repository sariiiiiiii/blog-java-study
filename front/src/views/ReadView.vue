<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  }
})

// template에서 mounted 안됐는데 data를 읽을려고 해서 초기화가 안됨 그래서 임의로 미리 초기화 작업
const post = ref({
  id: 0,
  title: "",
  content: "",
});

const moveToEdit = () => {
  router.push({name: 'edit', params: {postId: post.value.id}})
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
  })
})
</script>
<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>
      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2022-10-10 23:10:53</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>

</template>
<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0px;
}
.content {
  font-size: 0.95rem;
  margin-top: 8px;
  color: #5d5d5d;
  white-space: break-spaces;
  line-height: 1.5;
}
.sub {
  margin-top: 6px;
  font-size: 0.78rem;
  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
</style>
