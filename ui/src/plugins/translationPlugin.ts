import {translate} from "@/service/translationService.ts";
export default {
    install(app:any) {
        app.config.globalProperties.$translate = translate;
    }
};
