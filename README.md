# Credits
Vifez @ Kira Development

# Rules
If you would like to distribute a fork of yours, you are required to ask for confirmation from me on my Discord account (`vifez`), and under these conditions (unless specified otherwise by me):
- You shall not claim this as your own
- All credits must be given


# Dev Credits
CraftBukkit
TacoSpigot
 
If you submit a PR involving both Bukkit and CraftBukkit, it's appreciated if each PR links the other. Additionally, every reference to an obfuscated field/method in NMS should be marked with `// PAIL: Rename` and optionally a suggested name, to make mapping creation easier. E.g.:
```
    entity.k.get(i).f(); // PAIL Rename pathfinders, navigateToHome 
```
Also, make sure to include `// Craftbukkit` comments to indicate modified NMS sources.
