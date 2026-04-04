![modloader: fabric](https://img.shields.io/badge/modloader-fabric-dbd0b4)
![enviroment: server and client](https://img.shields.io/badge/environment-client%20and%20server-b01fe0)

# Unjank


> A set of QoL improvements, designed for datapacks.

> This mod depends on [owo-lib](https://modrinth.com/mod/owo-lib)

## Client-side Features

### Force close a GUI screen
Pressing `Alt+Escape` while in a screen/dialog, will force it to close.

> Access the config via `/owo-config unjank` or [Mod Menu](https://modrinth.com/mod/modmenu)


### Disable Focus Border (default = `True`)

Disables the white outline around text elements in dialogs.

> Disable Focus Border = `False`
![](./assets/screenshots/disabled_focus_border_off.png)

> Disable Focus Border = `True`
![](./assets/screenshots/disabled_focus_border_on.png)

### Disable Warning Box (default = `True`)

Disables the warning box on custom dialogs.

> Disable Warning Box = `False`
![](./assets/screenshots/disabled_warning_box_off.png)

> Disable Warning Box = `True`
![](./assets/screenshots/disabled_warning_box_on.png)

### Disable Command Warning Screen (default = `True`)

Disables the warning screen when running commands of op-level 1+.

> Disable Command Warning Screen = `False`
![](./assets/screenshots/disabled_command_warning_off.gif)

> Disable Command Warning Screen = `True`
![](./assets/screenshots/disabled_command_warning_on.gif)


## Server-side Features

### Gamerule `unjank:send_trigger_feedback` (default = `sourceAndOps`)

Controls the feedback of the `/trigger` command directly.

Possible values:

`disabled`: No command feedback shown to any player.

`sourceOnly`: Command feedback only show to the player that ran it.

`sourceAndOps`: Command feedback shown to the player that ran it *and* operators.

### Gamerule `unjank:broadcast_macro_failure` (default = `false`)

If enabled, macros that fail to be instantiated will be broadcast to *all* operators on the server.

### Gamerule `unjank:log_macro_failure` (default = `false`)

If enabled, macros that fail to be instantiated will be logged in the game's output log.