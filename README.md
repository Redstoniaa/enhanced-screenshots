# Snap!

_Snap!_ is a nice mod that adds some useful features when you're taking screenshots.

When you take a screenshot with this mod enabled, a nice little screen will show up with a couple options on what to do with it next. You can...
- **Change the file name that the image is being saved to.** If you find yourself with a lot of screenshots with names that look really similar, and you can't tell them apart, this will prove pretty useful, as it allows you to change what name the file is saved under!
- **Copy the image to your clipboard.** If you're the type of person to take screenshots and immediately want to send them to your friends over _insert messaging platform here,_ this gives you an easy way to get your image straight into your clipboard and do just that!
- **Discard the image.** Very useful if you're the type who often accidentally takes screenshots. _(you're not alone if you are, I do too!)_ This allows you to discard screenshots you don't like or didn't mean to take. This also works well with the copy utility, if you want to take a quick screenshot to send to your friends but don't want it to keep sitting around in your file system.

## Sounds great, how do I use it?
- **Snap is a Fabric mod**, and I have no plans to port to Forge, even though it'll probably be really easy. If you don't have Fabric yet, here's the [installer](https://fabricmc.net/use/installer/)!
  - [Quilt](https://quiltmc.org/) has support for Fabric mods, and most likely will support this one without any problems. If you do find problems though, feel free to [report them](../../issues)!
- **Snap currently supports Minecraft versions 1.19 and above**, and this was mainly because I got scared off by all my code turning red when going to 1.18. If you are playing in an older version and really want support for that version, you can yell at me about it in [the issues](../../issues).
- Once you've got your mod loader set up and are on a supported version, you can head over to the [releases](../../releases) and select the latest version of the mod, and select the correct version of the mod to match your Minecraft version. I will have a table there telling you what mod version works with what range of Minecraft versions for you to look at. Make sure you get the right one!
  - I plan to get this mod up on [Modrinth](https://modrinth.com/) at some point, so people can more easily download the mod, and this would also allow for nice things with [Prism Launcher](https://prismlauncher.org/) such as easy downloading and auto updating! 

## A guide to the screen
There are just four things on the screen. Nice and simple!
- A text field where you can specify what name you want your file to save under.
- The "Save" button, which just saves the file under whatever name you put in, or just how Minecraft normally saves screenshots if you left it blank. You can also press `Enter` to save the file.
- The "Copy to Clipboard" button, which copies the file to your clipboard. Sometimes this button may be greyed out and unavailable, which is something you should yell at me at to fix. You can do `Ctrl` + `C` if you hate your mouse.
- The "Discard" button, which gets rid of the screenshot, which can also be done with `Esc`.

## Further improvements for future me
I would like to add a sort of "screenshot display" on the screen, to show the player what the screenshot they just took looks like. I currently am skill issuing, so I cannot right now, but I'm looking to other mods to see how they've done it. At the moment, this isn't an issue in singleplayer, because the game will just pause in this screen, but in multiplayer, the game would keep going, so you don't get to see how your screenshot looks.

## Credits and attributions
Not many people directly contributed to this project, because, well - I kinda just start projects on a whim whenever I feel like it. Despite that, there are still a lot of people who _indirectly_ contributed, and I have much to thank them for!
- **Redstonia** - main (and only) developer of this mod. That's me!
- [**Fallen Breath**](https://github.com/Fallen-Breath) - their [yamlang](https://github.com/Fallen-Breath/yamlang) plugin, which converts `.yml` files to `.json` so Minecraft can read them, which is neat! Also for [preprocessor](https://github.com/Fallen-Breath/preprocessor), which allows for nice support for multiple versions of the game! I had to refer to their [fabric-mod-template](https://github.com/Fallen-Breath/fabric-mod-template) many times to understand how to actually use the preprocessor, so many thanks to their code, because I cannot Gradle!
  - [**Replay Mod team**](https://github.com/ReplayMod) - for the original [preprocessor](https://github.com/ReplayMod/preprocessor), of which Fallen's is a fork of.
- [**FabricMC team**](https://github.com/FabricMC) - Snap is of course, a Fabric mod. Thanks to you guys for making a great mod loader!
- [**QuiltMC team**](https://github.com/QuiltMC) - cool people with cool [mappings](https://github.com/QuiltMC/quilt-mappings)! Only reason that I'm not developing a Quilt mod is because Fabric is more widely used, but I have my fingers crossed that that shifts in the future, because you people are doing some really amazing stuff!
- [**Vineflower team**](https://github.com/Vineflower) - for making the fantastic [Vineflower](https://github.com/Vineflower/vineflower) decompiler, that produces quite readable code.

## Thanks for reading to the end!
_You're welcome!_