Pod::Spec.new do |spec|
    spec.name                     = 'rsa'
    spec.version                  = '1.6.0-alpha'
    spec.homepage                 = ''
    spec.source                   = { :http=> ''}
    spec.authors                  = 'IOG'
    spec.license                  = ''
    spec.summary                  = 'ApolloRSA is an RSA lib'
    spec.vendored_frameworks      = 'build/cocoapods/framework/ApolloRSA.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '13.0'
    spec.osx.deployment_target = '12.0'
    spec.tvos.deployment_target = '13.0'
    spec.watchos.deployment_target = '8.0'
    spec.dependency 'IOHKRSA', '1.0.0'
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':rsa',
        'PRODUCT_MODULE_NAME' => 'ApolloRSA',
    }
                
    spec.script_phases = [
        {
            :name => 'Build rsa',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end