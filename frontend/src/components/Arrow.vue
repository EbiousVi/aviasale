<template>
    <div class="arrow-left">
        <span></span>
        <span></span>
        <span></span>
    </div>
</template>

<script>
    export default {
        name: "Arrow"
    }
</script>

<style scoped>
    .arrow-left span {
        display: block;
        width: 15px;
        height: 15px;
        border-bottom: 3px solid #5897fb;
        border-left: 3px solid #5897fb;
        margin: -2px;
        animation: arrow-left 3s infinite;
        float: left;
    }

    .arrow-left span:nth-child(2){
        animation-delay: 0.1s;
    }
    .arrow-left span:nth-child(3){
        animation-delay: 0.2s;
    }
    @keyframes arrow-left {
        0%{
            opacity: 0;
            transform: rotate(-135deg) translate(-0px,-0px);
        }
        50%{
            opacity: 1;
        }
        100%{
            opacity: 0;
            transform: rotate(-135deg) translate(0px,0px);
        }
    }
</style>