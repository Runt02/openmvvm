## OpenMvvm 开源的mvvm项目

# 简介
    项目是基于mvvm模式开发，引用了Google新出的ViewBinding框架。为了提高开发效率，开发了这套开源项目。此项目是基于多年的工作经验总结出来的。代码高效、简洁，可大大提高Android开发人员的工作效率。

# 为什么用ViewBinding？
    为什么用ViewBinding而不是现在大部分人用的DataBinding？
        1.首先，我在用Android studio创建demo的时候，Google官方给的demo用的就是ViewBinding，基于此才使用的ViewBinding。既然Google推荐，我也就继续使用。
        2.其次，网上搜了相关帖子，我也简单总结了两者的优劣，参考文章（https://blog.csdn.net/Runt02/article/details/126330609）。

### 主要目录结构

```
com.runt.open.mvvm
│
├─base 封装类
│  ├─activities activity封装类
│  │  ├─ BaseActivity activity封装父类
│  │  ├─ BaseFragmentActivity 带有fragment切换的activity
│  │  ├─ BaseTabActivity 带有tablayout activity封装(带有viewpager的视图父类)
│  │  └─ LoadPageActivity 含有上拉刷新的分页Activity封装
│  ├─adapter 适配器封装类
│  │  ├─ BaseAdapter adapter封装父类
│  │  └─ FragmentAdapter 加载fragment适配器
│  ├─fragments fragment封装类
│  │  ├─ BaseFragment fragment封装父类
│  │  ├─ BaseTabFragment 带有tablayout fragment封装(带有viewpager的视图父类)
│  │  └─ LoadPageFragment 含有上拉刷新的分页fragment封装
│  └─model viewmodel封装类
│     ├─ BaseViewModel ViewModel封装父类
│     ├─ ImpViewModel ViewModel空实现
│     ├─ LoadPageViewModel 含有分页请求的ViewModel封装
│     └─ ViewModelFactory ViewModel工厂
│
├─conf 所有配置
│
├─data 数据bean
│  ├─ HttpApiResult 接口回到数据类
│  ├─ PageResult 分页数据类
│  ├─ PhoneDevice 设备信息数据类
│  └─ Results 所有数据类集合
│
├─retrofit 网络请求模块
│  ├─api 接口请求地址
│  ├─Interceptor 接口请求监听器（包含log打印）
│  └─observable.java 接口请求观察
│
├─ui 项目UI布局
│  ├─adapter 适配器
│  └─loadpage 含有分页的UI布局
│
├─util 工具类
│
└─widgets 自定义view


```
### 引入的第三方框架
    1、permissionx权限请求
    2、smartrefresh 下拉刷新
    3、okhttp+retrofit网络请求
    4、BottomMenu 底部菜单
    5、glide 图片加载
    6、PictureSelector 图片选择框架
    
## 项目特色
### 1.项目特别色
### 2.优化封装类中的泛型对象的实例化过程
    众多分装类都使用了泛型<T>，但是在实现的过程中都需要单独一个方法来创建对象。在我的项目里对此进行了优化。
    ---也就是说，继承带有泛型的封装类，不需要专门实现某个方法来创建对象。
#####  例如：MsgDetailActivity
    //继承了BaseActivity,声明了binding和viewmodel类，BaseActivity已经完成了对俩个对象的创建，代码中可以直接使用。
    class MsgDetailActivity extends BaseActivity<ActivityMsgDetailBinding,MsgDetailViewModel> {
        @Override
        public void initViews() {
            mViewModel.detailLive.observe(this, message -> {
                mBinding.txtMsgTitle.setText(message.title);
                mBinding.txtAuthor.setText(message.cUName);
                mBinding.txtTime.setText(HandleDate.getTimeStateNew(HandleDate.getDateTimeToLong(message.cTime))+" · "+getString(R.string.created_at));
                WebSettings settings = mBinding.txtContent.getSettings();
                settings.setTextZoom(80); // 通过百分比来设置文字的大小，默认值是100。
                settings.setDefaultTextEncodingName("UTF-8");
                mBinding.txtContent.loadData(message.content,"text/html","UTF-8");
            });
        }
    
        @Override
        public void loadData() {
            mViewModel.getMsgDetail(getIntent().getStringExtra("id"));
        }
    }

### 3.对下拉刷新、上拉加载分页数据进行封装
    很多开源项目并没有对带有分页数据请求和展示进行封装的，而此处恰恰有很多的封装优化控件。
    在本项目中对此进行了优化，大大减少了代码量，提高开发效率
##### 例 1：
    页面仅仅只是展示分页数据，没有太多复杂交互的情况下，HomeFragment继承LoadPageFragment，仅仅声明ViewBinding，ViewModel，Adapter和数据类就可以了，完全没有多余的代码实现
    class HomeFragment extends LoadPageFragment<RefreshRecyclerBinding, HomeViewModel, MsgAdapter, Message> { }

##### 例 2：
    页面除了展示分页数据，还有其他数据展示和交互的情况下，CoinRecordActivity继承LoadPageActivity，声明ViewBinding，ViewModel，Adapter和数据类
    还需要实现标题和标题栏右侧图片，也可以在xml中实现，仅需对应的ViewBinding即可。
    requestParams是接口请求传递的参数（默认page和size已经在ViewModel中）
    若有其他操作可在initViews和loadData中实现
    class CoinRecordActivity extends LoadPageActivity<ActivityRecyclerBinding, CoinRecordViewModel, CoinTransAdapter, CustomCoin>{
        @Override
        protected String initTitle() {
            return "金币记录";
        }

        @Override
        public void initViews() {
            super.initViews();
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_white_setting);
            drawable.setTint(getResources().getColor(R.color.txt_color));
            setTitleRight(drawable);
            titleBarView.setRightClick(new CustomClickListener() {
                @Override
                protected void onSingleClick(View view) {
                    startActivity(new Intent(mContext, CoinSettingActivity.class));//打开设置
                }
            });
        }
    }

##### loadPageViewModel实现更简洁 例如：
    仅需实现接口地址和数据类即可
    CoinRecordViewModel extends LoadPageViewModel<CustomCoin>{
        @Override
        public Observable<HttpApiResult<PageResult<Results.CustomCoin>>> request(int page, Object... objects) {
            return commonApi.getCoinRecord(page,SIZE,0);
        }
    }
### 4.可读性更强的接口请求数据日志打印
 ![接口请求数据打印](https://img-blog.csdnimg.cn/20210101160327602.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1J1bnQwMg==,size_16,color_FFFFFF,t_70)
# 结语
    虽然标题写的是mvvm模式，但不论从结构还是代码封装来看，跟mvp并没有核心的区别。我也看过很多mvvm的开源项目，结构都跟mvp没有本质的区别。
    mvvm最标准的结构还是Google官方login demo的
        其结构构成非常复杂，且流程化、标准化。东西太多，介绍不完。有兴趣的可以自己研究下

### 开发者

- QQ群：721765299
  <a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=5XjXWFh7YsRaofRoqoO6YRPzVE9ED0fA&jump_from=webapi"><img border="0" src="https://images.gitee.com/uploads/images/2019/0530/203513_ac6773bf_123301.png" alt="Android OpenMVVM" title="Android OpenMVVM"></a>