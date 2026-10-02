# FaClip API Reference

> 本文档基于 FaClip 源码自动生成，涵盖所有核心模块的类、注解、生命周期与协作方式。

---

## 目录

- [1. 架构总览](#1-架构总览)
- [2. faBootstrap — 启动入口](#2-fabootstrap--启动入口)
- [3. faInterface — 接口系统（核心）](#3-fainterface--接口系统核心)
- [4. faCommand — 声明式命令系统](#4-facommand--声明式命令系统)
- [5. faItem — 自定义物品系统](#5-faitem--自定义物品系统)
- [6. faWindow — GUI 窗口系统](#6-fawindow--gui-窗口系统)
- [7. faRecipe — 配方系统](#7-farecipe--配方系统)
- [8. faData — 数据管理](#8-fadata--数据管理)
- [9. faIoC — 控制反转容器](#9-faioc--控制反转容器)
- [10. faMessage — 消息与国际化](#10-famessage--消息与国际化)
- [11. faResource — 资源包管理](#11-faresource--资源包管理)

---

## 1. 架构总览

FaClip 采用 **模块化解耦 + 声明式驱动** 的架构设计。所有功能模块均实现 `FaModule` 接口，遵循统一的生命周期：

```
STOPPED → STARTING → READY → STOPPED
```

### FaModule 接口

```java
public interface FaModule {
    void auto();       // 初始化并启动模块
    void close();      // 关闭并清理资源
    Status status();   // 查询当前状态
    String getName();  // 模块名称

    enum Status { READY, STARTING, STOPPED }
}
```

### 模块协作关系

```
Fa.auto()
  ├── FaCmdInstance     (faCommand)  ──依赖──▶ FaInterfaceInstance
  ├── FaItemInstance    (faItem)     ──依赖──▶ FaInterfaceInstance
  ├── FaWindowInstance  (faWindow)   ──依赖──▶ FaItemInstance, FaInterfaceInstance
  └── FaRecipeInstance  (faRecipe)   ──依赖──▶ FaItemInstance, FaInterfaceInstance
```

所有模块共享 **faInterface（接口系统）** 作为事件/命令路由中枢。通过 `@Intf` 注解将静态方法注册为可被系统调用的接口处理函数。

### 核心设计理念

| 理念 | 说明 |
|------|------|
| **声明式优先** | 通过注解 + YAML 配置文件声明命令、物品、配方、GUI，无需手动注册 |
| **接口驱动** | 所有事件处理统一通过 `FaIntf` 接口系统路由，方法即处理器 |
| **文件驱动加载** | `.item` 文件定义物品、`.inv` 文件定义 GUI、`.re` 文件定义配方 |
| **零耦合 shade** | 以 fat jar 方式编译进目标插件，`Fa.auto()` 一行代码完成初始化 |

---

## 2. faBootstrap — 启动入口

### `Fa` 类

**包路径**: `com.wenkrang.faClip.module.faBootstrap.Fa`

`Fa` 是整个 FaClip 框架的入口类。使用者在插件的 `onEnable()` 中调用 `Fa.auto()` 即可完成所有模块的初始化。

#### 静态方法

| 方法 | 说明 |
|------|------|
| `Fa.auto()` | 自动检测调用者插件，初始化所有核心模块 |
| `Fa.close()` | 关闭所有模块，释放资源 |
| `Fa.cmd()` | 获取 `FaCmdInstance` 命令实例 |
| `Fa.item()` | 获取 `FaItemInstance` 物品实例 |
| `Fa.win()` | 获取 `FaWindowInstance` 窗口实例 |
| `Fa.recipe()` | 获取 `FaRecipeInstance` 配方实例 |
| `Fa.getPlugin()` | 获取当前绑定的插件实例 |

#### 初始化流程

```java
public static void auto() {
    plugin = PluginHelper.detectCallingPlugin();  // 自动检测调用者插件
    faCmdInstance = new FaCmdInstance(plugin);
    faItemInstance = new FaItemInstance(plugin);
    faWindowInstance = new FaWindowInstance(plugin, faItemInstance);
    faRecipeInstance = new FaRecipeInstance(plugin, faItemInstance);
    FaData.init(plugin);
    // 依次启动各模块
    faCmdInstance.auto();
    faItemInstance.auto();
    faWindowInstance.auto();
    faRecipeInstance.auto();
}
```

---

## 3. faInterface — 接口系统（核心）

faInterface 是 FaClip 的**事件路由中枢**，负责将注解标记的静态方法注册为可被命令、物品、窗口等系统调用的接口处理函数。

### 核心类

#### `FaIntf` — 接口对象

**包路径**: `com.wenkrang.faClip.module.faInterface.FaIntf`

代表一个已注册的接口处理函数，封装了方法引用、节点路径、参数转换器等信息。

| 方法 | 说明 |
|------|------|
| `getNode()` | 获取接口节点路径 |
| `getMethod()` | 获取绑定的 Java 方法 |
| `check(String[] args)` | 完整匹配检查（节点 + 参数） |
| `checkNode(String[] args)` | 仅节点匹配检查 |
| `checkParam(String[] args)` | 仅参数匹配检查 |
| `fuzzyCheck(String[] args)` | 模糊匹配（用于 Tab 补全） |
| `invoke(Object, FaIntfContext, String[])` | 反射调用绑定的方法 |

#### `FaInterfaceInstance` — 接口管理器

**包路径**: `com.wenkrang.faClip.module.faInterface.FaInterfaceInstance`

| 方法 | 说明 |
|------|------|
| `registerFaIntf(Method, String)` | 手动注册接口到指定节点 |
| `registerFaIntf(Method)` | 通过 `@Intf` 注解自动注册 |
| `enableForAll(Plugin)` | 扫描插件所有类，注册所有 `@Intf` 方法 |
| `enableFor(Class[])` | 注册指定类中的 `@Intf` 方法 |
| `guessIntf(String[])` | 根据参数猜测匹配的接口列表 |
| `getIntf(String)` | 根据节点精确查找接口 |

#### `FaIntfContext` — 接口调用上下文

**包路径**: `com.wenkrang.faClip.module.faInterface.FaIntfContext`

通用的键值上下文容器，在接口调用时传递事件、物品等上下文数据。

```java
FaIntfContext ctx = new FaIntfContext();
ctx.set("event", playerInteractEvent);
ctx.set("item", itemStack);
Player player = ctx.get("player");
```

#### `FaIntfCheckResult` — 匹配结果枚举

| 值 | 含义 |
|----|------|
| `FULL_MATCH` | 节点和参数完全匹配 |
| `PARAM_LAST_NO_MATCH` | 最后一个参数不匹配 |
| `PARAM_PARTIAL_NO_MATCH` | 部分参数不匹配 |
| `PARAM_NO_MATCH` | 参数完全不匹配 |
| `NODE_FULL_MATCH` | 节点完全匹配 |
| `NODE_LAST_NO_MATCH` | 最后一个节点段不匹配 |
| `NODE_PARTIAL_NO_MATCH` | 部分节点段匹配 |
| `NODE_NO_MATCH` | 节点完全不匹配 |
| `NO_MATCH` | 完全不匹配 |

### 注解

#### `@Intf`

**包路径**: `com.wenkrang.faClip.module.faInterface.annotation.Intf`

标记一个**静态方法**为 FaClip 接口处理函数。

| 属性 | 类型 | 说明 |
|------|------|------|
| `value()` | `String` | 接口节点路径（如 `"myplugin.command.teleport"`） |

**要求**：
- 方法必须是 `static`
- 方法参数类型必须有对应的 `SimpleParam` 转换器
- 可插入 `FaIntfContext` 参数获取上下文（不计入节点匹配）

**示例**：
```java
@Intf("myplugin.teleport")
public static void onTeleport(FaIntfContext ctx, String world, int x) {
    Player player = ctx.get("player");
    // ...
}
```

---

## 4. faCommand — 声明式命令系统

faCommand 提供基于注解的声明式命令注册，开发者只需编写方法并添加注解，框架自动完成命令注册、权限检查、Tab 补全和_HELP 生成。

### 核心类

#### `FaCmd` — 命令对象

**包路径**: `com.wenkrang.faClip.module.faCommand.FaCmd`

不可变的命令封装，通过 `FaCmd.Builder` 构建。包含命令节点、权限、描述等元数据。

| 方法 | 说明 |
|------|------|
| `getNode()` | 命令节点路径（如 `"plugin.tp"`） |
| `getName()` | 命令名称（节点最后一段） |
| `getDescription()` | 命令描述 |
| `getPermission()` | 权限节点 |
| `isRequireOP()` | 是否需要 OP |
| `isForPlayer()` | 是否仅限玩家 |
| `isOnlyForHelp()` | 是否仅为帮助命令 |
| `canExecute(CommandSender)` | 检查发送者是否有权限执行 |
| `getRejectReason(CommandSender)` | 获取拒绝原因（i18n key） |
| `getRootNode()` | 获取根节点（第一段） |

#### `FaCmdInstance` — 命令管理器

**包路径**: `com.wenkrang.faClip.module.faCommand.FaCmdInstance`

实现 `FaModule`，管理所有已注册的命令。

| 方法 | 说明 |
|------|------|
| `enableFor(Class[])` | 注册指定类中的命令方法 |
| `enableForAll()` | 扫描插件所有类，注册所有命令 |
| `getFaCmd(String)` | 根据节点获取命令列表 |
| `getFaCmd(FaIntf)` | 根据接口获取命令 |
| `getFaCmds()` | 获取所有已注册命令 |
| `addFaCmd(FaCmd)` | 手动添加命令 |
| `removeFaCmd(FaCmd)` | 移除命令 |
| `setDebugMode(boolean)` | 启用/关闭调试模式 |

#### `FaCmdInterpreter` — 命令解释器

**包路径**: `com.wenkrang.faClip.module.faCommand.interpreter.FaCmdInterpreter`

负责命令的注册解析、执行和 Tab 补全。内部维护两条管线：

- **解释器管线**（interpreterPipe）：`EmptyCheck → ConflictCheck → Authorization → OnlyForHelp`
- **补全管线**（tabPipe）：`TabAuth`

#### `FaCmdContext` — 命令上下文

**包路径**: `com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext`

继承 `FaIntfContext`，提供命令执行时的上下文信息。

```java
CommandSender sender = context.sender();
String[] args = context.args();
```

### 注解一览

| 注解 | 目标 | 说明 |
|------|------|------|
| `@Cmd("node.path")` | 方法 | **必需**。声明命令节点路径，用 `.` 分隔层级 |
| `@CmdPermission("perm.node")` | 方法 | 设置命令所需权限节点 |
| `@RequireOP` | 方法 | 标记需要 OP 权限才能执行 |
| `@ForPlayer` | 方法 | 限制仅玩家可执行 |
| `@Help("usage info")` | 方法 | 设置命令帮助信息 |
| `@OnlyForHelp` | 方法 | 标记为纯帮助节点，不可执行 |
| `@Debug` | 方法 | 标记为调试命令（仅在 debug 模式下注册） |
| `@ParamDes("desc")` | 参数 | 描述单个参数的含义 |
| `@ParamArrayDes({"a","b"})` | 参数 | 描述数组参数的可选值 |
| `@CustomDes(Provider.class)` | 参数 | 使用自定义 `DesProvider` 动态生成描述 |

### 命令注册流程

```
方法扫描 → @Cmd 识别 → 注解 Handler 链填充 Builder
  → FaIntf 注册 → FaCmd 构建 → 根命令注册到 Bukkit CommandMap
  → 解释器管线拦截执行
```

### 完整示例

```java
public class MyCommands {

    @Cmd("myplugin.teleport")
    @CmdPermission("myplugin.tp")
    @ForPlayer
    @Help("传送到指定坐标: /myplugin teleport <world> <x> <y> <z>")
    public static void teleport(
            FaIntfContext ctx,
            @ParamDes("世界名") String world,
            @ParamDes("X坐标") int x,
            @ParamDes("Y坐标") int y,
            @ParamDes("Z坐标") int z
    ) {
        Player player = (Player) ctx.get("sender");
        player.teleport(new Location(
            Bukkit.getWorld(world), x, y, z
        ));
        Fm.info(player, "已传送到 " + world + " (" + x + ", " + y + ", " + z + ")");
    }

    @Cmd("myplugin.admin.reload")
    @RequireOP
    @Help("重载插件配置")
    public static void reload(FaIntfContext ctx) {
        // 重载逻辑
        Fm.info("配置已重载");
    }
}
```

---

## 5. faItem — 自定义物品系统

faItem 提供基于 YAML 文件（`.item`）的声明式物品定义，支持标签管理、事件绑定和物品分组。

### 核心类

#### `FaItem` — 自定义物品

**包路径**: `com.wenkrang.faClip.module.faItem.FaItem`

继承 `ItemStack`，扩展了命名空间键、标签管理、事件绑定和分组功能。

| 方法 | 说明 |
|------|------|
| `copy()` | 深拷贝物品 |
| `getTagMgr()` | 获取标签管理器 |
| `setEvent(FaItemEvent, String)` | 绑定事件到接口节点 |
| `setGroup(List<String>)` | 设置物品分组 |
| `getGroup()` | 获取物品分组 |
| `setIsolate(boolean)` | 设置是否隔离（防止堆叠） |
| `getIsolate()` | 获取隔离状态 |
| `setTemplate(String)` / `getTemplate()` | 设置/获取模板 ID |

#### `FaItemInstance` — 物品管理器

**包路径**: `com.wenkrang.faClip.module.faItem.FaItemInstance`

实现 `FaModule`，负责从插件资源中加载 `.item` 文件并管理所有已注册的 FaItem。

| 方法 | 说明 |
|------|------|
| `load(String path)` | 从指定资源路径加载物品定义 |
| `loadAll()` | 扫描并加载所有 `.item` 文件 |
| `getFaItem(String key)` | 根据键名获取物品（返回副本） |
| `setFaItem(String key, FaItem)` | 手动注册物品 |
| `getGroup(String key)` | 根据分组名获取所有物品 |
| `convertDefine(String)` | 解析物品定义字符串（如 `"MC.DIAMOND*5"` 或 `"myitem"`） |
| `registerHandler(Class...)` | 注册物品事件处理器 |

#### `FaItemEvent` — 物品事件枚举

| 值 | 说明 |
|----|------|
| `ITEM_CLICK` | 物品右击/交互事件 |
| `INV_CLICK` | 物品在背包中被点击事件 |

#### `TagMgr` — 标签管理器

**包路径**: `com.wenkrang.faClip.module.faItem.TagMgr`

基于 `PersistentDataContainer`（PDC）的物品标签读写工具。

| 方法 | 说明 |
|------|------|
| `set(String key, String value)` | 设置标签 |
| `get(String key)` | 获取标签值 |
| `has(String key)` | 检查标签是否存在 |
| `remove(String key)` | 移除标签 |

### 物品定义文件（`.item`）

物品通过 YAML 文件定义，放置在插件 JAR 资源目录中，启动时自动扫描加载。

```yaml
id: "magic_sword"
name: "§6魔法剑"
material: DIAMOND_SWORD
lore:
  - "§7一把蕴含魔力的剑"
  - "§7攻击力 +10"
group:
  - "weapon"
  - "magic"
events:
  item_click: "myplugin.magic_sword.click"
```

### 事件处理

物品事件通过 `@Intf` 注解绑定到接口方法：

```java
@Intf("myplugin.magic_sword.click")
public static void onMagicSwordClick(FaIntfContext ctx) {
    PlayerInteractEvent event = ctx.get("event");
    ItemStack item = ctx.get("item");
    Player player = event.getPlayer();
    Fm.info(player, "你使用了魔法剑！");
}
```

---

## 6. faWindow — GUI 窗口系统

faWindow 提供基于 `.inv` 文件的声明式 GUI 界面定义，支持字符画布局、物品定义映射、事件绑定和界面模板。

### 核心类

#### `FaInventory` — 界面定义对象

**包路径**: `com.wenkrang.faClip.module.faWindow.FaInventory`

由 `.inv` 文件解析生成的界面模板，包含布局设计、物品定义、事件绑定等。

| 方法 | 说明 |
|------|------|
| `event(String eventID, String intf)` | 绑定事件到接口 |
| `define(String key, ItemStack)` | 定义字符对应的物品 |
| `design(List<String>)` | 设置布局设计（字符画） |
| `note(String key, List<Integer>)` | 设置标签（标记特定槽位） |
| `set(int slot, ItemStack)` | 覆盖指定槽位的物品 |
| `get(int slot)` | 获取指定槽位的物品 |
| `clone()` | 深拷贝生成独立实例 |
| `getTemplate()` | 获取模板界面 |

#### `FaWindowInstance` — 窗口管理器

**包路径**: `com.wenkrang.faClip.module.faWindow.FaWindowInstance`

实现 `FaModule`，管理所有 GUI 模板的加载、渲染和事件。

| 方法 | 说明 |
|------|------|
| `load(String path)` | 从资源路径加载 `.inv` 文件 |
| `loadAll()` | 扫描并加载所有 `.inv` 文件 |
| `getFaInventory(String id)` | 根据 ID 获取界面模板（返回副本） |
| `render(FaInventory)` | 渲染界面为 Bukkit `Inventory` |
| `setData(FaInventory, FaInventoryData, FaWindow)` | 设置界面数据 |
| `register(Class...)` | 注册界面事件处理器 |

#### `FaWindow` — 窗口会话

**包路径**: `com.wenkrang.faClip.module.faWindow.FaWindow`

代表一个玩家的窗口会话，管理界面打开、回指和临时存储。

| 方法 | 说明 |
|------|------|
| `open(FaInventory)` | 打开一个界面模板 |
| `open(Inventory)` | 打开已有的 Inventory |
| `setMem(String id, T value)` | 设置会话临时变量 |
| `getMem(String id)` | 获取会话临时变量 |
| `setBackRef(Inventory)` | 设置返回引用（用于多级界面） |

#### `FaInventoryData` — 界面数据容器

**包路径**: `com.wenkrang.faClip.module.faData.FaInventoryData`

实现 `InventoryHolder`，作为 GUI 的数据载体绑定到 Inventory 上。

```java
FaInventoryData data = new FaInventoryData();
data.set("key", "value");
String val = data.get("key");
```

### 界面定义文件（`.inv`）

```yaml
id: "main_menu"
name: "§1主菜单"
size: 27
lock: true
design:
  - "AAAAAAAAA"
  - "A       A"
  - "AAAAAAAAA"
define:
  A: "MC.STONE"
events:
  click: "myplugin.menu.click"
note:
  border: [0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26]
```

### 打开界面示例

```java
// 在命令处理中
FaWindow window = new FaWindow(Fa.win(), player);
FaInventory template = Fa.win().getFaInventory("main_menu");
FaInventoryData data = window.open(template);
data.set("win", window);  // 保存窗口引用
```

---

## 7. faRecipe — 配方系统

faRecipe 提供基于 `.re` 文件的声明式配方定义，支持自定义合成、熔炼配方及配方事件。

### 核心类

#### `FaRecipe` — 配方对象

**包路径**: `com.wenkrang.faClip.module.faRecipe.FaRecipe`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `String` | 配方唯一标识 |
| `recipe` | `Recipe` | Bukkit 配方对象 |
| `type` | `String` | 配方类型 |
| `template` | `String` | 模板 ID |
| `events` | `Map<String,String>` | 事件绑定表 |

#### `FaRecipeInstance` — 配方管理器

**包路径**: `com.wenkrang.faClip.module.faRecipe.FaRecipeInstance`

实现 `FaModule`，管理所有配方的加载、注册和事件。

| 方法 | 说明 |
|------|------|
| `load(String path)` | 从资源路径加载配方定义 |
| `loadAll()` | 扫描并加载所有 `.re` 文件 |
| `addRecipe(NamespacedKey, Recipe)` | 注册原生 Bukkit 配方 |
| `addRecipe(NamespacedKey, FaRecipe)` | 注册 FaRecipe 并记录事件 |
| `getRecipe(String key)` | 根据 ID 获取配方 |
| `getRecipes()` | 获取所有已注册配方 |
| `register(Class...)` | 注册配方事件处理器 |

### 配方事件

| 事件类 | 说明 |
|--------|------|
| `ReCraftE` | 合成事件监听 |
| `ReCookE` | 熔炼事件监听 |
| `ReCrafterCraftE` | 1.21.4+ Crafter 合成事件 |
| `RePlayerJoinE` | 玩家加入时解锁配方 |

---

## 8. faData — 数据管理

faData 提供基于 YAML 的分层数据管理方案，涵盖配置、玩家数据、物品数据和临时数据。

### 核心类

#### `FaData` — YAML 数据工具类

**包路径**: `com.wenkrang.faClip.module.faData.FaData`

封装 `YamlConfiguration`，提供便捷的数据读写、保存、重载、合并等操作。

**构造方式**：

| 构造器 | 说明 |
|--------|------|
| `FaData(File)` | 从文件初始化 |
| `FaData(File, String)` | 从目录 + 文件名初始化 |
| `FaData(String node)` | 从节点路径初始化（自动定位到插件 data 目录） |
| `FaData(InputStream)` | 从输入流初始化（只读） |

**核心方法**：

| 方法 | 说明 |
|------|------|
| `set(String, Object)` | 设置值 |
| `get(String)` / `getOrDefault(...)` | 获取值 |
| `has(String)` | 检查路径是否存在 |
| `remove(String)` | 移除路径 |
| `save()` | 保存到文件 |
| `reload()` | 从文件重载 |
| `merge(YamlConfiguration, boolean)` | 合并配置 |
| `template(FaData)` | 从模板补全缺失键 |
| `getString/getInt/getBoolean/getDouble/getLong(...)` | 类型安全的读取 |
| `getSection(String)` / `createSection(String)` | 节点操作 |
| `getKeys(boolean)` | 获取所有键 |

#### `FaConfig` — 插件配置

```java
FaConfig config = new FaConfig();  // 自动加载 config.yaml
boolean debug = config.getBoolean("enableDebugger");
```

#### `FaPlayerData` — 玩家持久数据

```java
FaPlayerData data = new FaPlayerData(player, "settings");
data.set("language", "zh_CN");
data.save();
```

存储路径：`data/player/{uuid}/settings.yaml`

#### `FaItemData` — 物品持久数据

基于物品 PDC 中的 UUID 为每个物品实例创建独立的数据文件。

```java
FaItemData itemData = FaItemData.create(itemStack);
itemData.set("durability", 100);
itemData.save();
```

#### `FaPlayerTempData` — 玩家临时数据

内存级键值存储，插件重启后数据丢失。

```java
FaPlayerTempData.set(player, "selectedMenu", "main");
String menu = FaPlayerTempData.get(player, "selectedMenu");
FaPlayerTempData.remove(player, "selectedMenu");
```

---

## 9. faIoC — 控制反转容器

faIoC 提供轻量级的依赖注入（IoC/DI）容器，支持自动扫描、构造注入、字段注入。

### 注解

| 注解 | 目标 | 说明 |
|------|------|------|
| `@Service` | 类 | 标记为受管 Bean，支持 `SINGLETON`（默认）和 `PROTOTYPE` 作用域 |
| `@Autowired` | 字段 / 构造器 / 参数 | 标记需要自动注入的依赖 |
| `@Qualifier("name")` | 字段 / 参数 | 指定注入的 Bean 限定名 |

### 核心类

#### `FaIoCInstance` — IoC 容器

**包路径**: `com.wenkrang.faClip.module.faIoC.FaIoCInstance`

实现 `FaModule`，管理所有受管 Bean 的生命周期。

| 方法 | 说明 |
|------|------|
| `auto()` | 扫描所有 `@Service` 类并注册 |
| `load(Class<?>)` | 手动加载一个类到容器 |
| `getContext(Class<?>)` | 获取指定类的 IoC 对象 |
| `hasContext(Class<?>)` | 检查是否已注册 |
| `addEnvInstance(Object)` | 添加外部环境实例（如 Plugin） |

#### `FaIoCObject` — Bean 包装

**包路径**: `com.wenkrang.faClip.module.faIoC.FaIoCObject`

容器中的 Bean 包装对象，通过 Builder 模式构建。

### 使用示例

```java
@Service
public class PlayerService {
    @Autowired
    private DataService dataService;

    public void savePlayerData(Player player) {
        // dataService 已自动注入
        dataService.save(player);
    }
}

@Service
public class DataService {
    public void save(Player player) {
        // 保存逻辑
    }
}
```

---

## 10. faMessage — 消息与国际化

### `Fm` — 消息工具类

**包路径**: `com.wenkrang.faClip.module.faMessage.Fm`

提供统一的日志输出和错误上报接口，支持控制台、玩家、CommandSender 三种目标。

| 方法 | 标记 | 说明 |
|------|------|------|
| `Fm.log(msg)` | 无 | 普通日志 |
| `Fm.info(msg)` | 蓝色 `[*]` | 信息状态 |
| `Fm.warning(msg)` | 黄色 `[!]` | 警告 |
| `Fm.error(msg)` | 红色 `[-]` | 错误 |
| `Fm.debug(msg)` | 灰色 `[/]` | 调试信息 |
| `Fm.detail(...)` | 格式化多行 | 详细报告 |
| `Fm.reportError(key, args)` | — | i18n 错误上报 |
| `Fm.reportWarning(key, args)` | — | i18n 警告上报 |
| `Fm.reportError(Throwable)` | — | 异常上报（可选堆栈） |

所有方法均有 `(Player)` 和 `(CommandSender)` 的重载版本。

### 国际化（i18n）

通过 `I18nHelper` 实现多语言支持：

- `language.properties` — 语言选择
- `language_zh_CN.properties` — 中文
- `language_en_US.properties` — 英文

```java
String msg = I18nHelper.t("FaCommand.Error.Interpreter.RequireOP");
String formatted = I18nHelper.ft("key.with.args", arg1, arg2);
```

---

## 11. faResource — 资源包管理

### 核心类

#### `BukkitResource` — 资源包信息

**包路径**: `com.wenkrang.faClip.module.faResource.BukkitResource`

| 字段 | 类型 | 说明 |
|------|------|------|
| `name` | `String` | 资源包名称 |
| `url` | `String` | 下载地址 |
| `id` | `UUID` | 基于 SHA1 生成的唯一 ID |
| `sha` | `byte[]` | SHA1 哈希值 |

#### `FaBukkitResourceManager` — 资源包管理器

**包路径**: `com.wenkrang.faClip.module.faResource.FaBukkitResourceManager`

| 方法 | 说明 |
|------|------|
| `registerResource(BukkitResource)` | 注册资源包 |
| `unregisterResource(BukkitResource)` | 注销资源包 |
| `getResource(String name)` | 按名称查找资源包 |
| `getResource(UUID uuid)` | 按 UUID 查找资源包 |
| `askFor(BukkitResource, Player, String)` | 请求玩家加载资源包 |
| `loadFor(BukkitResource, Player, String)` | 直接为玩家加载资源包 |

### 使用示例

```java
FaBukkitResourceManager manager = new FaBukkitResourceManager(plugin);
BukkitResource pack = new BukkitResource(
    "MyTexturePack",
    "https://example.com/pack.zip",
    "a1b2c3d4e5f6..."  // SHA1
);
manager.registerResource(pack);
manager.askFor(pack, player, "§6需要加载材质包");
```

---

## 附录：异常体系

FaClip 定义了统一的异常层次，所有异常均携带 i18n 键：

| 异常类 | 说明 |
|--------|------|
| `FaException` | 基础异常（标记接口） |
| `FaCmdException` | 命令系统异常 |
| `FaDataException` | 数据系统异常 |
| `FaItemException` | 物品系统异常 |
| `FaIntfException` | 接口系统异常 |
| `FaResourceException` | 资源系统异常 |
