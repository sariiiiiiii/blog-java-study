import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    proxy: {
        "/api":
            { target: "http://localhost:8080/", rewrite: (path: String) => path.replace(/^\/api/, ""), // /api로 시작하는 path를 읽어서 실제로 보낼때는 /api <- String을 ""로 대체
            } // /posts요청이 날라오면 localhost:8080으로 보내겠다 (vite server 설정)
    }
  }
});
