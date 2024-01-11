var app = Vue.createApp({
  data() {
    return ${initData};
  },
  template: `${vueTemplate}`,
  methods: {
    cb(route, ev) {
      if (arguments.length==0) {
        route=""
        ev={}
      } else if (arguments.length==1) {
        if (typeof(route)=="object") {
          ev=route;
          route="";
        } else {
          ev={}
        }
      }
      Object.assign(ev, {route: route, state: this.$data});
      var evJson=JSON.stringify(ev);
      ${ajaxCall}
    },
    ${vueMethods}
 },
 mounted() {
   this.$el.parentElement.myVueInst=this;
 }
});
${useLibs}
app.mount('#${appDivId}')