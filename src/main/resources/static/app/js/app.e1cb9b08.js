(function(e){function t(t){for(var o,i,r=t[0],l=t[1],s=t[2],u=0,d=[];u<r.length;u++)i=r[u],Object.prototype.hasOwnProperty.call(c,i)&&c[i]&&d.push(c[i][0]),c[i]=0;for(o in l)Object.prototype.hasOwnProperty.call(l,o)&&(e[o]=l[o]);p&&p(t);while(d.length)d.shift()();return a.push.apply(a,s||[]),n()}function n(){for(var e,t=0;t<a.length;t++){for(var n=a[t],o=!0,i=1;i<n.length;i++){var r=n[i];0!==c[r]&&(o=!1)}o&&(a.splice(t--,1),e=l(l.s=n[0]))}return e}var o={},i={app:0},c={app:0},a=[];function r(e){return l.p+"js/"+({"admin~movie":"admin~movie",admin:"admin",movie:"movie",login:"login"}[e]||e)+"."+{"admin~movie":"d5c754fd",admin:"de5ce7d4",movie:"24ee60d8",login:"977cff9f"}[e]+".js"}function l(t){if(o[t])return o[t].exports;var n=o[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,l),n.l=!0,n.exports}l.e=function(e){var t=[],n={"admin~movie":1,admin:1,movie:1,login:1};i[e]?t.push(i[e]):0!==i[e]&&n[e]&&t.push(i[e]=new Promise((function(t,n){for(var o="css/"+({"admin~movie":"admin~movie",admin:"admin",movie:"movie",login:"login"}[e]||e)+"."+{"admin~movie":"0b6dcaa6",admin:"0553d2a9",movie:"091f1242",login:"3d07550f"}[e]+".css",c=l.p+o,a=document.getElementsByTagName("link"),r=0;r<a.length;r++){var s=a[r],u=s.getAttribute("data-href")||s.getAttribute("href");if("stylesheet"===s.rel&&(u===o||u===c))return t()}var d=document.getElementsByTagName("style");for(r=0;r<d.length;r++){s=d[r],u=s.getAttribute("data-href");if(u===o||u===c)return t()}var p=document.createElement("link");p.rel="stylesheet",p.type="text/css",p.onload=t,p.onerror=function(t){var o=t&&t.target&&t.target.src||c,a=new Error("Loading CSS chunk "+e+" failed.\n("+o+")");a.code="CSS_CHUNK_LOAD_FAILED",a.request=o,delete i[e],p.parentNode.removeChild(p),n(a)},p.href=c;var m=document.getElementsByTagName("head")[0];m.appendChild(p)})).then((function(){i[e]=0})));var o=c[e];if(0!==o)if(o)t.push(o[2]);else{var a=new Promise((function(t,n){o=c[e]=[t,n]}));t.push(o[2]=a);var s,u=document.createElement("script");u.charset="utf-8",u.timeout=120,l.nc&&u.setAttribute("nonce",l.nc),u.src=r(e);var d=new Error;s=function(t){u.onerror=u.onload=null,clearTimeout(p);var n=c[e];if(0!==n){if(n){var o=t&&("load"===t.type?"missing":t.type),i=t&&t.target&&t.target.src;d.message="Loading chunk "+e+" failed.\n("+o+": "+i+")",d.name="ChunkLoadError",d.type=o,d.request=i,n[1](d)}c[e]=void 0}};var p=setTimeout((function(){s({type:"timeout",target:u})}),12e4);u.onerror=u.onload=s,document.head.appendChild(u)}return Promise.all(t)},l.m=e,l.c=o,l.d=function(e,t,n){l.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},l.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},l.t=function(e,t){if(1&t&&(e=l(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(l.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var o in e)l.d(n,o,function(t){return e[t]}.bind(null,o));return n},l.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return l.d(t,"a",t),t},l.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},l.p="",l.oe=function(e){throw console.error(e),e};var s=window["webpackJsonp"]=window["webpackJsonp"]||[],u=s.push.bind(s);s.push=t,s=s.slice();for(var d=0;d<s.length;d++)t(s[d]);var p=u;a.push([0,"chunk-vendors"]),n()})({0:function(e,t,n){e.exports=n("56d7")},"0771":function(e,t,n){},"24cd":function(e,t,n){"use strict";n("cd93")},"42af":function(e,t,n){"use strict";n("7fff")},"434e":function(e,t,n){"use strict";n("612c")},"50ae":function(e,t,n){"use strict";n.d(t,"a",(function(){return r}));n("d3b7");var o=n("d4ec"),i=n("bee2"),c=n("bc3a"),a=n.n(c),r=function(){function e(t){Object(o["a"])(this,e),this.auth=t,this.createAxios()}return Object(i["a"])(e,[{key:"createAxios",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:function(e){return Promise.reject(e)};return this.api=a.a.create({baseURL:"",timeout:3e3,withCredentials:!1}),this.api.interceptors.response.use((function(e){return e}),e),this}},{key:"logout",value:function(){this.axios.headers={}}},{key:"baseUrl",get:function(){return""}},{key:"headers",get:function(){return{Authorization:"Bearer "+(this.auth?this.auth.jwt:"")}}},{key:"axios",get:function(){return this.api}}]),e}()},"56d7":function(e,t,n){"use strict";n.r(t);n("caad"),n("b680"),n("2532"),n("e260"),n("e6cf"),n("cca6"),n("a79d");var o=n("7a23"),i=(n("d3b7"),n("6c02")),c=[{path:"/",name:"movie",component:function(){return Promise.all([n.e("admin~movie"),n.e("movie")]).then(n.bind(null,"578a"))}},{path:"/movie",name:"movie2",component:function(){return Promise.all([n.e("admin~movie"),n.e("movie")]).then(n.bind(null,"578a"))}},{path:"/login",name:"login",component:function(){return n.e("login").then(n.bind(null,"61b1"))},props:!1},{path:"/admin/:view",name:"view",component:function(){return Promise.all([n.e("admin~movie"),n.e("admin")]).then(n.bind(null,"d824"))},props:!0}],a=Object(i["a"])({history:Object(i["b"])(),routes:c}),r=a,l={class:"layout-main"};function s(e,t,n,i,c,a){var r=Object(o["resolveComponent"])("AppTopBar"),s=Object(o["resolveComponent"])("Toast"),u=Object(o["resolveComponent"])("AppProfile"),d=Object(o["resolveComponent"])("AppMenu"),p=Object(o["resolveComponent"])("router-view"),m=Object(o["resolveComponent"])("AppFooter");return Object(o["openBlock"])(),Object(o["createBlock"])("div",{class:a.containerClass,onClick:t[2]||(t[2]=function(){return a.onWrapperClick.apply(a,arguments)})},[Object(o["createVNode"])(r,{onMenuToggle:a.onMenuToggle},null,8,["onMenuToggle"]),Object(o["createVNode"])(s),Object(o["createVNode"])(o["Transition"],{name:"layout-sidebar"},{default:Object(o["withCtx"])((function(){return[Object(o["withDirectives"])(Object(o["createVNode"])("div",{class:a.sidebarClass,onClick:t[1]||(t[1]=function(){return a.onSidebarClick.apply(a,arguments)})},[Object(o["createVNode"])(u,{user:c.user},null,8,["user"]),Object(o["createVNode"])(d,{model:e.menu,onMenuitemClick:a.onMenuItemClick,key:a.menuKey},null,8,["model","onMenuitemClick"])],2),[[o["vShow"],a.isSidebarVisible()]])]})),_:1}),Object(o["createVNode"])("div",l,[Object(o["createVNode"])(p)]),Object(o["createVNode"])(m)],2)}n("a15b"),n("4d63"),n("ac1f"),n("25f0"),n("3ca3"),n("5319"),n("841c"),n("1276"),n("ddb0"),n("2b3d");var u=Object(o["withScopeId"])("data-v-6eab6f23");Object(o["pushScopeId"])("data-v-6eab6f23");var d={class:"layout-topbar"},p=Object(o["createVNode"])("span",{class:"pi pi-bars"},null,-1),m={class:"page-title"},b={class:"layout-topbar-icons"},f={key:0,class:"p-link",title:"Logout"},h=Object(o["createVNode"])("span",{class:"layout-topbar-item-text"},"Logout",-1),v={key:1,class:"p-link",title:"Login"},g=Object(o["createVNode"])("span",{class:"layout-topbar-item-text"},"Login",-1);Object(o["popScopeId"])();var j=u((function(e,t,n,i,c,a){return Object(o["openBlock"])(),Object(o["createBlock"])("div",d,[Object(o["createVNode"])("button",{class:"p-link layout-menu-button",onClick:t[1]||(t[1]=function(){return a.onMenuToggle.apply(a,arguments)})},[p]),Object(o["createVNode"])("div",m,Object(o["toDisplayString"])(a.pageTitle),1),Object(o["createVNode"])("div",b,[a.isLoggedIn?(Object(o["openBlock"])(),Object(o["createBlock"])("button",f,[h,Object(o["createVNode"])("span",{class:"layout-topbar-icon pi pi-sign-out",onClick:t[2]||(t[2]=function(){return a.logout.apply(a,arguments)})})])):Object(o["createCommentVNode"])("",!0),a.isLoggedIn?Object(o["createCommentVNode"])("",!0):(Object(o["openBlock"])(),Object(o["createBlock"])("button",v,[g,Object(o["createVNode"])("span",{class:"layout-topbar-icon pi pi-sign-in",onClick:t[3]||(t[3]=function(){return a.login.apply(a,arguments)})})]))])])})),y={methods:{onMenuToggle:function(e){this.$emit("menu-toggle",e)},login:function(){this.$router.push("/login")},logout:function(){this.$store.commit("logout"),this.$router.push("/login")}},computed:{isLoggedIn:function(){return this.$store.getters.auth.isLoggedIn},pageTitle:function(){return this.$store.getters.pageTitle}}};n("7664");y.render=j,y.__scopeId="data-v-6eab6f23";var O=y,k=Object(o["withScopeId"])("data-v-f89382fe");Object(o["pushScopeId"])("data-v-f89382fe");var w={class:"layout-profile"},C={class:"username"},M=Object(o["createVNode"])("i",{class:"pi pi-fw pi-cog"},null,-1),N={key:1},S=Object(o["createTextVNode"])("Login"),I=Object(o["createVNode"])("i",{class:"pi pi-fw pi-power-off"},null,-1),A=Object(o["createVNode"])("span",null,"Logout",-1);Object(o["popScopeId"])();var V=k((function(e,t,n,i,c,a){var r=Object(o["resolveComponent"])("Button");return Object(o["openBlock"])(),Object(o["createBlock"])("div",w,[Object(o["createVNode"])("div",null,[Object(o["createVNode"])("img",{src:a.profilePic,alt:"",style:{width:"120px",height:"80px"}},null,8,["src"])]),a.auth.isLoggedIn?(Object(o["openBlock"])(),Object(o["createBlock"])("button",{key:0,class:"p-link layout-profile-link",onClick:t[1]||(t[1]=function(){return a.onClick.apply(a,arguments)})},[Object(o["createVNode"])("span",C,Object(o["toDisplayString"])(a.auth.user.fullName),1),M])):(Object(o["openBlock"])(),Object(o["createBlock"])("div",N,[Object(o["createVNode"])(r,{class:"p-link layout-profile-link",onClick:t[2]||(t[2]=function(e){return a.login()})},{default:k((function(){return[S]})),_:1})])),Object(o["createVNode"])(o["Transition"],{name:"layout-submenu-wrapper"},{default:k((function(){return[Object(o["withDirectives"])(Object(o["createVNode"])("ul",null,[Object(o["createVNode"])("li",null,[Object(o["createVNode"])("button",{class:"p-link",onClick:t[3]||(t[3]=function(){return a.logout.apply(a,arguments)})},[I,A])])],512),[[o["vShow"],c.expanded]])]})),_:1})])})),B=n("6549"),L=n.n(B),x={components:{Button:L.a},props:{user:Object},data:function(){return{expanded:!1}},computed:{isLoggedIn:function(){return this.auth.isLoggedIn},auth:function(){return this.$store.getters.auth},profilePic:function(){return this.isLoggedIn&&this.auth.user.picture?this.auth.user.picture:"assets/layout/images/no-profile-pic.jpg"}},methods:{onClick:function(e){this.expanded=!this.expanded,e.preventDefault()},login:function(){this.$router.push({path:"/login"})},logout:function(){this.$store.commit("logout"),this.expanded=!1,this.$router.push("/login")}}};n("42af");x.render=V,x.__scopeId="data-v-f89382fe";var $=x,T=Object(o["withScopeId"])("data-v-51bcdb84");Object(o["pushScopeId"])("data-v-51bcdb84");var D={class:"layout-menu-container"};Object(o["popScopeId"])();var P=T((function(e,t,n,i,c,a){var r=Object(o["resolveComponent"])("AppSubmenu");return Object(o["openBlock"])(),Object(o["createBlock"])("div",D,[Object(o["createVNode"])(r,{items:c.menu,class:"layout-menu",root:!0,onMenuitemClick:a.onMenuItemClick},null,8,["items","onMenuitemClick"])])})),_=Object(o["withScopeId"])("data-v-1acbc8df");Object(o["pushScopeId"])("data-v-1acbc8df");var E={key:0},K={key:0,class:"arrow"},F={key:0,class:"pi pi-fw pi-angle-down menuitem-toggle-icon"},R={key:1,class:"menuitem-badge"},J={key:0,class:"pi pi-fw pi-angle-down menuitem-toggle-icon"},U={key:1,class:"menuitem-badge"};Object(o["popScopeId"])();var W=_((function(e,t,n,i,c,a){var r=Object(o["resolveComponent"])("router-link"),l=Object(o["resolveComponent"])("AppSubmenu"),s=Object(o["resolveDirective"])("ripple");return n.items?(Object(o["openBlock"])(),Object(o["createBlock"])("ul",E,[(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(n.items,(function(i,u){return Object(o["openBlock"])(),Object(o["createBlock"])(o["Fragment"],null,[a.visible(i)&&!i.separator?(Object(o["openBlock"])(),Object(o["createBlock"])("li",{key:u,class:[{"active-menuitem":c.activeIndex===u&&!i.to&&!i.disabled}],role:"none"},[i.items&&!0===n.root?(Object(o["openBlock"])(),Object(o["createBlock"])("div",K)):Object(o["createCommentVNode"])("",!0),i.to?Object(o["withDirectives"])((Object(o["openBlock"])(),Object(o["createBlock"])(r,{key:1,to:i.to,class:[i.class,"p-ripple",{"active-route":c.activeIndex===u,"p-disabled":i.disabled}],style:i.style,onClick:function(e){return a.onMenuItemClick(e,i,u)},target:i.target,exact:"",role:"menuitem"},{default:_((function(){return[Object(o["createVNode"])("i",{class:i.icon},null,2),Object(o["createVNode"])("span",null,Object(o["toDisplayString"])(i.label),1),i.items?(Object(o["openBlock"])(),Object(o["createBlock"])("i",F)):Object(o["createCommentVNode"])("",!0),i.badge?(Object(o["openBlock"])(),Object(o["createBlock"])("span",R,Object(o["toDisplayString"])(i.badge),1)):Object(o["createCommentVNode"])("",!0)]})),_:2},1032,["to","class","style","onClick","target"])),[[s]]):Object(o["createCommentVNode"])("",!0),i.to?Object(o["createCommentVNode"])("",!0):Object(o["withDirectives"])((Object(o["openBlock"])(),Object(o["createBlock"])("a",{key:2,href:i.url||"#",style:i.style,class:[i.class,"p-ripple",{"p-disabled":i.disabled}],onClick:function(e){return a.onMenuItemClick(e,i,u)},target:i.target,role:"menuitem"},[Object(o["createVNode"])("i",{class:i.icon},null,2),Object(o["createVNode"])("span",null,Object(o["toDisplayString"])(i.label),1),i.items?(Object(o["openBlock"])(),Object(o["createBlock"])("i",J)):Object(o["createCommentVNode"])("",!0),i.badge?(Object(o["openBlock"])(),Object(o["createBlock"])("span",U,Object(o["toDisplayString"])(i.badge),1)):Object(o["createCommentVNode"])("",!0)],14,["href","onClick","target"])),[[s]]),Object(o["createVNode"])(o["Transition"],{name:"layout-submenu-wrapper"},{default:_((function(){return[Object(o["withDirectives"])(Object(o["createVNode"])(l,{items:a.visible(i)&&i.items,onMenuitemClick:t[1]||(t[1]=function(t){return e.$emit("menuitem-click",t)})},null,8,["items"]),[[o["vShow"],c.activeIndex===u]])]})),_:2},1024)],2)):Object(o["createCommentVNode"])("",!0),a.visible(i)&&i.separator?(Object(o["openBlock"])(),Object(o["createBlock"])("li",{class:"p-menu-separator",style:i.style,key:"separator"+u,role:"separator"},null,4)):Object(o["createCommentVNode"])("",!0)],64)})),256))])):Object(o["createCommentVNode"])("",!0)})),q={name:"AppSubmenu",props:{items:Array,root:{type:Boolean,default:!1}},data:function(){return{activeIndex:null}},methods:{onMenuItemClick:function(e,t,n){t.disabled?e.preventDefault():(t.to||t.url||e.preventDefault(),t.command&&t.command({originalEvent:e,item:t}),this.activeIndex=n===this.activeIndex?null:n,this.$emit("menuitem-click",{originalEvent:e,item:t}))},visible:function(e){return"function"===typeof e.visible?e.visible():!1!==e.visible}},components:{AppSubmenu:void 0}};n("434e");q.render=W,q.__scopeId="data-v-1acbc8df";var z=q,G={props:{model:Array},data:function(){return{menu:[{label:"Movies",icon:"pi pi-fw pi-video",to:"/movie"},{label:"Admin",icon:"pi pi-fw pi-sitemap",visible:!1,items:[{label:"Change Log",icon:"pi pi-fw pi-pencil",to:"/admin/changelog"},{label:"Rent Log",icon:"pi pi-fw pi-dollar",to:"/admin/rentlog"},{label:"Purchase Log",icon:"pi pi-fw pi-shopping-cart",to:"/admin/purchaselog"}]},{label:"API Docs",icon:"pi pi-fw pi-file",url:"/api-docs"}]}},methods:{onMenuItemClick:function(e){this.$emit("menuitem-click",e)}},mounted:function(){this.menu[1].visible=this.$store.getters.auth.isAdmin},components:{AppSubmenu:z},computed:{auth:function(){return this.$store.getters.auth}}};n("f03a");G.render=P,G.__scopeId="data-v-51bcdb84";var H=G,Q=Object(o["withScopeId"])("data-v-7aee346f");Object(o["pushScopeId"])("data-v-7aee346f");var X={class:"layout-footer"},Y=Object(o["createVNode"])("img",{src:"assets/layout/images/video-camera-icon-72.png",alt:"DVD Rental",width:"40"},null,-1),Z=Object(o["createVNode"])("span",{class:"footer-text",style:{"margin-left":"5px"}},"DVD Rental Client",-1);Object(o["popScopeId"])();var ee=Q((function(e,t,n,i,c,a){return Object(o["openBlock"])(),Object(o["createBlock"])("div",X,[Y,Z])})),te={name:"AppFooter"};n("24cd");te.render=ee,te.__scopeId="data-v-7aee346f";var ne=te,oe={setup:function(){},mounted:function(){window.Android&&window.addEventListener("load",(function(){window.Android.finishLoading()}));var e=new URLSearchParams(window.location.search).get("token");e&&this.$router.push("/login")},data:function(){return{user:{},layoutMode:"static",layoutColorMode:"dark",staticMenuInactive:!1,overlayMenuActive:!1,mobileMenuActive:!1}},watch:{$route:function(){this.menuActive=!1,this.$toast.removeAllGroups()}},methods:{onWrapperClick:function(){this.menuClick||(this.overlayMenuActive=!1,this.mobileMenuActive=!1),this.menuClick=!1},onMenuToggle:function(){this.menuClick=!0,this.isDesktop()?"overlay"===this.layoutMode?(!0===this.mobileMenuActive&&(this.overlayMenuActive=!0),this.overlayMenuActive=!this.overlayMenuActive,this.mobileMenuActive=!1):"static"===this.layoutMode&&(this.staticMenuInactive=!this.staticMenuInactive):this.mobileMenuActive=!this.mobileMenuActive,event.preventDefault()},onSidebarClick:function(){this.menuClick=!0},onMenuItemClick:function(e){e.item&&!e.item.items&&(this.overlayMenuActive=!1,this.mobileMenuActive=!1)},onLayoutChange:function(e){this.layoutMode=e},onLayoutColorChange:function(e){this.layoutColorMode=e},addClass:function(e,t){e.classList?e.classList.add(t):e.className+=" "+t},removeClass:function(e,t){e.classList?e.classList.remove(t):e.className=e.className.replace(new RegExp("(^|\\b)"+t.split(" ").join("|")+"(\\b|$)","gi")," ")},isDesktop:function(){return window.innerWidth>1024},isSidebarVisible:function(){return!this.isDesktop()||("static"===this.layoutMode?!this.staticMenuInactive:"overlay"!==this.layoutMode||this.overlayMenuActive)}},computed:{containerClass:function(){return["layout-wrapper",{"layout-overlay":"overlay"===this.layoutMode,"layout-static":"static"===this.layoutMode,"layout-static-sidebar-inactive":this.staticMenuInactive&&"static"===this.layoutMode,"layout-overlay-sidebar-active":this.overlayMenuActive&&"overlay"===this.layoutMode,"layout-mobile-sidebar-active":this.mobileMenuActive,"p-input-filled":"filled"===this.$appState.inputStyle,"p-ripple-disabled":!1===this.$primevue.ripple}]},sidebarClass:function(){return["layout-sidebar",{"layout-sidebar-dark":"dark"===this.layoutColorMode,"layout-sidebar-light":"light"===this.layoutColorMode}]},logo:function(){return this.layoutColorMode,"assets/layout/images/video-camera-white.png"},isAdmin:function(){return this.auth.isAdmin},auth:function(){return this.$store.getters.auth},menuKey:function(){return this.$store.getters.menuKey}},beforeUpdate:function(){this.mobileMenuActive?this.addClass(document.body,"body-overflow-hidden"):this.removeClass(document.body,"body-overflow-hidden")},components:{AppTopBar:O,AppProfile:$,AppMenu:H,AppFooter:ne}};n("9cdc");oe.render=s;var ie=oe,ce=n("f350"),ae=n.n(ce),re=n("6060"),le=n.n(re),se=n("8459"),ue=n.n(se),de=n("40f3"),pe=n.n(de),me=n("5502"),be=n("d4ec"),fe=n("bee2"),he=function(){function e(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:null,n=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{};Object(be["a"])(this,e),this.login(t,null==n?{}:n)}return Object(fe["a"])(e,[{key:"login",value:function(e,t){var n=this;n.jwt=e,n.user=t,localStorage.setItem("jwt",n.jwt),localStorage.setItem("user",JSON.stringify(n.user))}},{key:"logout",value:function(){this.jwt="",this.user={},this.updateLocalStorage()}},{key:"updateLocalStorage",value:function(){localStorage.setItem("jwt",this.jwt),localStorage.setItem("user",JSON.stringify(this.user))}},{key:"isLoggedIn",get:function(){return!!this.jwt}},{key:"isAdmin",get:function(){return!(!this.isLoggedIn||!this.user.admin)}}]),e}(),ve=n("50ae"),ge=new me["a"]({state:function(){return{menuKey:0,auth:new he(localStorage.getItem("jwt"),JSON.parse(localStorage.getItem("user")?localStorage.getItem("user"):"{}")),services:{},pageTitle:"Movies"}},getters:{isAdmin:function(e){return e.auth.isAdmin},isLoggedIn:function(e){return e.auth.isLoggedIn},auth:function(e){return e.auth},services:function(e){return e.services},menuKey:function(e){return e.menuKey},pageTitle:function(e){return e.pageTitle}},mutations:{setAuth:function(e,t){e.auth.login(t.jwt,t.user)},incrementMenuKey:function(e){e.menuKey++},logout:function(e){var t=new ve["a"](e.auth);t&&t.logout(),e.auth.logout(),e.menuKey++},pageTitle:function(e,t){e.pageTitle=t}}}),je=ge,ye=n("6de2"),Oe=n.n(ye),ke=n("c197"),we=n.n(ke),Ce={beforeMount:function(e,t){t.modifiers.script?e.className="language-javascript":t.modifiers.css?e.className="language-css":e.className="language-markup",we.a.highlightElement(e.children[0])}},Me=Ce;n("098b"),n("e1ae"),n("bddf"),n("4121"),n("6b2c"),n("6ef4"),n("c381"),n("f4f4"),n("0771"),n("b2ca");r.beforeEach((function(e,t,n){window.scrollTo(0,0),n()}));var Ne=Object(o["createApp"])(ie);Ne.config.globalProperties.$appState=Object(o["reactive"])({inputStyle:"outlined"}),Ne.config.globalProperties.$primevue=Object(o["reactive"])({ripple:!0}),Ne.use(ue.a),Ne.use(r),Ne.use(je),Ne.directive("tooltip",pe.a),Ne.directive("ripple",ae.a),Ne.directive("code",Me),Ne.component("Sidebar",Oe.a),Ne.component("Toast",le.a),Ne.mount("#app"),Ne.config.globalProperties.$filters={formatCurrency:function(e){return"$"+e.toFixed(2)}},Ne.config.globalProperties.$messages={showSuccess:function(e,t){t.$toast.add({severity:"success",summary:"Success",detail:e,life:3e3})},showError:function(e,t){t.$toast.add({severity:"error",summary:"Error",detail:e,life:3e3})}},Ne.config.globalProperties.$utils={isMobile:function(){return navigator.userAgent.includes("Mobile")}}},"612c":function(e,t,n){},7095:function(e,t,n){},7664:function(e,t,n){"use strict";n("7e13")},"7e13":function(e,t,n){},"7fff":function(e,t,n){},"9cdc":function(e,t,n){"use strict";n("c701")},b2ca:function(e,t,n){},c701:function(e,t,n){},cd93:function(e,t,n){},f03a:function(e,t,n){"use strict";n("7095")}});
//# sourceMappingURL=app.e1cb9b08.js.map