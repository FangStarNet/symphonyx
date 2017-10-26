# ！！！这个项目目前已经暂停维护，请使用 [Sym](https://github.com/b3log/symphony)！！！

----

# [SymphonyX](https://github.com/FangStarNet/symphonyx) [![Build Status](https://img.shields.io/travis/FangStarNet/symphonyx.svg?style=flat)](https://travis-ci.org/FangStarNet/symphonyx)

## 简介

SymX 是 [Sym](https://github.com/b3log/symphony) 的企业版，实现企业内网论坛。

## 对 Sym 的改造

### 增强

#### 航海日记

航海日记相当于日报+周报。

用户每天为段落，一天为一节，一周为一章。将每个人在公司的成长经历记录下，旨在帮助小伙伴了解公司历史以及其他小伙伴工作内容和进度。

 * 用户可配置所在团队
 * 特殊的帖子类型：航海日记段落、航海日记节/章（节/章为底层类型，界面上不可见）
 * 每日以管理员账号自动生成节，内容为空（留待管理员更新），在浏览时以内容+本日所有航海日记段落
 * 每周以管理员账号自动生成章，内容为空（留待管理员更新），在浏览时以内容+本周的所有航海日记节索引+节内容（段落内容汇总）
 * 航海日记章也包含搜索功能，可按时间段/人/团队进行展现

#### 商城

企业内的小卖部，以积分作为硬通货。

 * 充值管理
 * 商品管理
 * 订单管理

#### 搜索 TBD

 * 去除百度站内搜索
 * 实现新的搜索，过滤条件：
   * 关键字（标题、标签、内容）全文检索
   * 时间范围
   * 作者

### 其他

 * 去除 [B3log 构思](https://hacpai.com/b3log)
