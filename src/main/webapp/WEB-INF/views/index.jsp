<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="wro" uri="https://github.com/lifus/wro4j-runtime-taglib" %>

<html>
<head>
    <title>jsp width vue.js</title>
</head>
<body>
    <div id="myApp">
        <h2>index.jsp</h2>
        <div>{{ loaded }}</div>
        <button type="button" @click="clickButton">인사 버튼</button>
    </div>

    <div id="messageApp">
        <message-component />
    </div>
</body>
</html>

<wro:script group="vue"/>

<c:set var="scripts" scope="request">
    <script>
        const { loadModule } = window['vue3-sfc-loader'];

        Vue.createApp({
            data() {
                return {
                    loaded: false
                }
            },
            methods: {
                clickButton() {
                    alert('헬로월드!');
                }
            }
        }).mount('#myApp');

        const options = {
            moduleCache: {
                vue: Vue
            },
            async getFile(url) {
                const res = await fetch(url);
                if (!res.ok) {
                    throw Object.assign(new Error(res.statusText + ' ' + url), { res });
                }
                return {
                    getContentData: (asBinary) => asBinary ? res.arrayBuffer() : res.text()
                }
            },
            addStyle(textContent) {
                const style = Object.assign(document.createElement('style'), { textContent });
                const ref = document.head.getElementsByTagName('style')[0] || null;
                document.head.insertBefore(style, ref);
            }
        };

        Vue.createApp({
            components: {
                'message-component': Vue.defineAsyncComponent(() => loadModule('${pageContext.request.contextPath}/assets/dev/vue/pages/index.vue', options))
            }
        }).mount('#messageApp');

    </script>
</c:set>