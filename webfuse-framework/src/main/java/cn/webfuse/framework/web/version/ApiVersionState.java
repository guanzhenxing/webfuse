package cn.webfuse.framework.web.version;

import lombok.Data;

@Data
public class ApiVersionState {

    private int version;

    private boolean deprecated;

    public static class ApiVersionStateBuilder {

        private ApiVersion apiVersion;
        private Integer packageVersion;
        private Integer minimumVersion;

        public ApiVersionStateBuilder apiVersion(ApiVersion apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public ApiVersionStateBuilder packageVersion(Integer packageVersion) {
            this.packageVersion = packageVersion;
            return this;
        }

        public ApiVersionStateBuilder minimumVersion(int minimumVersion) {
            this.minimumVersion = minimumVersion;
            return this;
        }

        public ApiVersionState build() {
            ApiVersionState apiVersionState = new ApiVersionState();
            initVersion(apiVersionState);
            initDeprecated(apiVersionState);
            return apiVersionState;
        }

        private void initDeprecated(ApiVersionState apiVersionState) {
            //判断当前版本是否小于最低版本
            if (apiVersionState.getVersion() < minimumVersion) {
                apiVersionState.setDeprecated(true);
            }
        }

        private void initVersion(ApiVersionState apiVersionState) {
            //如果当前没有注解
            if (apiVersion == null) {
                //判断当前package的版本是否存在，如果存在设置
                if (this.packageVersion != null) {
                    apiVersionState.setVersion(this.packageVersion);
                } else {
                    //如果package的版本不存在，设置成默认1
                    apiVersionState.setVersion(1);
                }
            } else {//如果当前存在注解
                //如果当前版本号为0，说明没有设置版本
                if (apiVersion.value() == 0) {
                    //判断当前package的版本是否存在，如果存在设置。
                    if (this.packageVersion != null) {
                        apiVersionState.setVersion(this.packageVersion);
                    } else {//如果package的版本不存在，注解的版本也不存在，设置默认为1
                        apiVersionState.setVersion(1);
                    }
                } else {
                    //如果当前版本号不为0，说明设置了版本，设置即可
                    apiVersionState.setVersion(apiVersion.value());
                }
            }
        }

    }


}
