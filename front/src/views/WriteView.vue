<script setup lang="ts">
import {ref} from "vue"
import axios from 'axios'
import {useRouter} from "vue-router";

const title = ref("")
const content = ref("")

const router = useRouter();

const write = function () {
  axios.post("/api/v2/test", { // vite.config.ts에서 설정을 해줬기 때문에 domain은 생략
    title: title.value,
    content: content.value
  })
      .then(() => {
        router.replace({name: "home"});
      })
}

</script>

<template>

  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">작성완료</el-button>
    </div>
  </div>

</template>
<style>

</style>