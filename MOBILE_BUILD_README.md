# Bird Client — mobile build package

This package is prepared so GitHub Actions can compile the Fabric mod for you.
You do NOT need to install Java or Gradle on your phone.

## Build on your phone

1. Create a GitHub repository.
2. Upload all files from this project to the repository.
3. Open the repository's **Actions** tab.
4. Select **Build Bird Client**.
5. Tap **Run workflow**.
6. Wait for the build to finish.
7. Open the completed workflow run.
8. Under **Artifacts**, download `bird-client-jar`.
9. Extract the artifact and copy the generated `.jar` to your Zalith Fabric 1.21.11 mods folder.

## Important

The project must contain `gradlew` and `gradle/wrapper/` for the workflow to work.
If they are missing from the original source package, run this project once on a computer with a compatible Fabric Loom setup, or use the Gradle wrapper files from the exact project template/version being used.

Minecraft/Fabric versions must match your Zalith profile.
