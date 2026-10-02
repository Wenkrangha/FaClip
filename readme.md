# FaClip

[![](https://www.jitpack.io/v/Wenkrangha/FaClip.svg)](https://www.jitpack.io/#Wenkrangha/FaClip)
[![License](https://img.shields.io/github/license/Wenkrangha/FaClip)](LICENSE)

> **简化流程 · 提升效率 · 回归创作**

FaClip 是一个面向 Minecraft 插件开发者的 **Java 基础设施库**，集成了声明式命令、自定义物品、GUI 窗口、合成配方、数据管理、控制反转（IoC）等核心系统。

通过注解驱动和 YAML 配置声明，FaClip 将繁琐的 Bukkit API 注册流程简化为几行代码，让你专注于玩法创意而非底层样板。

---

## 核心特性

- **声明式命令系统** — 通过 `@Cmd`、`@CmdPermission`、`@ForPlayer` 等注解直接标记方法，自动完成命令注册、权限校验、Tab 补全和帮助生成
- **自定义物品系统** — 使用 `.item` YAML 文件定义物品，支持 PDC 标签管理、事件绑定（`ITEM_CLICK` / `INV_CLICK`）和物品分组
- **GUI 窗口系统** — 使用 `.inv` YAML 文件以字符画方式定义界面布局，支持模板继承、槽位锁定、事件绑定和多级界面跳转
- **配方系统** — 使用 `.re` YAML 文件声明合成/熔炼配方，内置版本兼容（1.21.4+ Crafter 支持）
- **数据管理** — 封装 YAML 读写，提供 `FaPlayerData`（玩家持久数据）、`FaItemData`（物品实例数据）、`FaPlayerTempData`（临时数据）等分层方案
- **接口系统（FaInterface）** — 统一的事件路由中枢，所有模块通过 `@Intf` 注解将静态方法注册为可被系统调用的处理函数
- **控制反转（IoC）** — 轻量级 DI 容器，支持 `@Service`、`@Autowired` 注解，自动扫描与依赖注入
- **消息与国际化** — 内置 `Fm` 消息工具类和 i18n 多语言支持
- **资源包管理** — 简化服务端资源包（Resource Pack）的注册与下发流程

---

## 安装指南

FaClip 通过 [JitPack](https://jitpack.io/#Wenkrangha/FaClip) 发布，以 shade（fat jar）方式编译进你的插件中使用。

### Gradle (Groovy DSL)

```groovy
// settings.gradle 或根 build.gradle
repositories {
    maven { url 'https://jitpack.io' }
}

// build.gradle
dependencies {
    implementation 'com.github.Wenkrangha:FaClip:0.1'
}
```

### Gradle (Kotlin DSL)

```kotlin
// settings.gradle.kts 或根 build.gradle.kts
repositories {
    maven("https://jitpack.io")
}

// build.gradle.kts
dependencies {
    implementation("com.github.Wenkrangha:FaClip:0.1")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Wenkrangha</groupId>
        <artifactId>FaClip</artifactId>
        <version>0.1</version>
    </dependency>
</dependencies>
```

> **注意**：FaClip 需要 Java 21+ 和 Spigot API 1.21.8+。

---

## 快速开始

### 1. 初始化框架

在你的插件主类的 `onEnable()` 中调用 `Fa.auto()` 即可一行完成所有模块的初始化：

```java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Fa.auto();  // 自动检测当前插件，初始化所有模块
    }

    @Override
    public void onDisable() {
        Fa.close();
    }
}
```

### 2. 注册一个带权限的命令

使用 `@Cmd` 注解标记静态方法，框架自动注册命令、校验权限、生成帮助信息：

```java
public class AdminCommands {

    @Cmd("myplugin.kick")
    @CmdPermission("myplugin.admin.kick")
    @ForPlayer
    @Help("踢出玩家: /myplugin kick <玩家名> [原因]")
    public static void kickPlayer(
            FaIntfContext ctx,
            @ParamDes("目标玩家") String targetName,
            @ParamDes("踢出原因") String reason
    ) {
        Player sender = (Player) ctx.get("sender");
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            Fm.error(sender, "玩家 " + targetName + " 不在线");
            return;
        }

        target.kickPlayer(reason);
        Fm.info(sender, "已踢出玩家 " + targetName);
    }

    @Cmd("myplugin.admin.reload")
    @RequireOP
    @Help("重载插件配置")
    public static void reload(FaIntfContext ctx) {
        // 重载配置逻辑
        Fm.info("配置已重载");
    }
}
```

### 3. 创建带交互事件的自定义物品

首先，在插件资源目录创建 `.item` 文件（如 `items/magic_wand.item`）：

```yaml
id: "magic_wand"
name: "§d魔法杖"
material: BLAZE_ROD
lore:
  - "§7右键释放魔法"
  - "§7冷却: 5秒"
group:
  - "magic"
  - "weapon"
events:
  item_click: "myplugin.magic_wand.use"
```

然后，用 `@Intf` 注解注册点击事件处理器：

```java
public class MagicWandHandler {

    @Intf("myplugin.magic_wand.use")
    public static void onMagicWandUse(FaIntfContext ctx) {
        PlayerInteractEvent event = ctx.get("event");
        Player player = event.getPlayer();

        // 发射火球
        player.launchProjectile(Fireball.class);
        Fm.info(player, "魔法已释放！");
    }
}
```

在代码中获取已注册的物品：

```java
FaItem wand = Fa.item().getFaItem("magic_wand");
player.getInventory().addItem(wand);
```

### 4. 构建一个简单的 GUI 窗口

创建 `.inv` 文件（如 `windows/shop.inv`）：

```yaml
id: "shop_main"
name: "§2服务器商店"
size: 27
lock: true
design:
  - "AAAAAAAAA"
  - "A B C D E A"
  - "AAAAAAAAA"
define:
  A: "MC.GRAY_STAINED_GLASS_PANE"
  B: "MC.DIAMOND"
  C: "MC.GOLD_INGOT"
  D: "MC.IRON_INGOT"
  E: "MC.EMERALD"
events:
  click: "myplugin.shop.click"
note:
  items: [11,12,13,14,15]
```

打开界面：

```java
public class ShopCommands {

    @Cmd("myplugin.shop")
    @ForPlayer
    @Help("打开服务器商店")
    public static void openShop(FaIntfContext ctx) {
        Player player = (Player) ctx.get("sender");
        FaWindow window = new FaWindow(Fa.win(), player);
        FaInventory template = Fa.win().getFaInventory("shop_main");
        window.open(template);
    }
}
```

### 5. 使用 IoC 容器管理依赖

```java
@Service
public class PlayerDataService {
    public void save(Player player) {
        FaPlayerData data = new FaPlayerData(player, "profile");
        data.set("level", 10);
        data.save();
    }
}

@Service
public class WelcomeService {
    @Autowired
    private PlayerDataService dataService;

    public void welcome(Player player) {
        dataService.save(player);
        Fm.info(player, "欢迎回来，" + player.getName() + "！");
    }
}
```

---

## 项目结构

```
FaClip/
├── src/main/java/com/wenkrang/faClip/
│   ├── helper/              # 通用工具类
│   ├── manager/             # 命令、事件、配方管理器
│   └── module/
│       ├── faBootstrap/     # 启动入口（Fa.auto()）
│       ├── faCommand/       # 声明式命令系统
│       ├── faData/          # YAML 数据管理
│       ├── faDebugger/      # 内部调试工具
│       ├── faInterface/     # 接口路由系统（核心）
│       ├── faIoC/           # 控制反转容器
│       ├── faItem/          # 自定义物品系统
│       ├── faMessage/       # 消息与国际化
│       ├── faRecipe/        # 配方系统
│       ├── faResource/      # 资源包管理
│       └── faWindow/        # GUI 窗口系统
└── src/main/resources/
    ├── language.properties          # 语言配置
    ├── language_zh_CN.properties    # 中文语言包
    └── language_en_US.properties    # 英文语言包
```

---

## 相关链接

- [API Reference（API 参考文档）](docs/api/API_REFERENCE.md) — 完整的模块 API 文档
- [JitPack 仓库](https://jitpack.io/#Wenkrangha/FaClip) — 查看可用版本与构建状态
- [GitHub 仓库](https://github.com/Wenkrangha/FaClip) — 源码、Issue 与贡献
- [Spigot MC](https://www.spigotmc.org/) — Minecraft 服务端 API

---

## 许可证

本项目基于 [LICENSE](LICENSE) 开源许可发布。
