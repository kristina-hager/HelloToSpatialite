# HelloToSpatialite

HelloToSpatialite is a relatively simple 'hello world' application illustrating the usage of [spatialite android](https://www.gaia-gis.it/fossil/libspatialite/wiki?name=splite-android).

I am attempting to follow, as much as possible, the [original spatialite android tutorial] (https://www.gaia-gis.it/fossil/libspatialite/wiki?name=spatialite-android-tutorial)
while making changes to bring this tutorial into the more recent Android Studio (v1.3) build environments.

This example relies on the Android module [Spatialite-Database-Driver](https://github.com/kristina-hager/Spatialite-Database-Driver).
Therefore, you will also need to get a copy of that lib:

`git clone https://github.com/kristina-hager/Spatialite-Database-Driver.git`

This example assumes you've put the directory for 'Spatialite-Database-Driver' in the same directory as 'HelloToSpatialite'.

# Changes to default Android project to enable usage of [Spatialite-Database-Driver](https://github.com/kristina-hager/Spatialite-Database-Driver)

[app/build.gradle](https://github.com/kristina-hager/HelloToSpatialite/blob/master/app/build.gradle)
- Add dependency on Spatialite-Database-Driver

`compile project(':..:Spatialite-Database-Driver:spatialite-db-driver')`

- Add gradle code to build in shared objects
  -  code copied from  [geopaparazzi.app/build.gradle](https://github.com/geopaparazzi/geopaparazzi/blob/master/geopaparazzi.app/build.gradle)

```
task copyNativeLibs(type: Copy) {
    from(new File(project(':..:Spatialite-Database-Driver:spatialite-db-driver').projectDir, 'src/main/java/jniLibs')) {
        include 'armeabi/libjsqlite.so'
        include 'armeabi-v7a/libjsqlite.so'
    }
    into new File(buildDir, 'native-libs')
}
tasks.withType(JavaCompile) { compileTask -> compileTask.dependsOn copyNativeLibs }
clean.dependsOn 'cleanCopyNativeLibs'

tasks.withType(PackageApplication) { pkgTask ->
    //pkgTask.jniDir new File(buildDir, 'native-libs')
    pkgTask.jniFolders = new HashSet<File>()
    pkgTask.jniFolders.add(new File(buildDir, 'native-libs'))
}
```
