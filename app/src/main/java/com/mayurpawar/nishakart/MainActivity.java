package com.mayurpawar.nishakart;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.text.InputType;
import org.json.*;
import java.util.*;

public class MainActivity extends Activity {
    final int NAVY=Color.rgb(17,27,48), ORANGE=Color.rgb(244,123,77), BG=Color.rgb(247,248,251), INK=Color.rgb(23,32,51), MUTED=Color.rgb(116,128,150), LINE=Color.rgb(232,235,241);
    LinearLayout root, content, header;
    EditText search;
    String category="All", filter="All";
    ArrayList<Product> products=new ArrayList<>(), cartProducts=new ArrayList<>();
    HashMap<Integer,Integer> cart=new HashMap<>();
    HashSet<Integer> wishlist=new HashSet<>();
    ArrayList<String> orders=new ArrayList<>();
    android.content.SharedPreferences prefs;
    int nextId=13;
    static class Product {
        int id,price,old; String name,category,emoji,tag; double rating;
        Product(int i,String n,String c,int p,int o,String e,String t,double r){id=i;name=n;category=c;price=p;old=o;emoji=e;tag=t;rating=r;}
    }
    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(NAVY);getWindow().setNavigationBarColor(NAVY);prefs=getSharedPreferences("nisha_kart",MODE_PRIVATE);seed();restore();showHome();}
    void seed(){
        products.add(new Product(1,"Studio Wireless Headphones","Electronics",1899,2499,"🎧","BESTSELLER",4.7));
        products.add(new Product(2,"Everyday Classic Sneakers","Fashion",1299,1799,"👟","POPULAR",4.5));
        products.add(new Product(3,"Minimal Ceramic Table Lamp","Home",899,1199,"💡","NEW",4.6));
        products.add(new Product(4,"Hydrating Skin Care Set","Beauty",649,899,"🧴","DEAL",4.4));
        products.add(new Product(5,"Smart Fitness Watch","Electronics",2199,2999,"⌚","TRENDING",4.3));
        products.add(new Product(6,"Soft Cotton Everyday Tee","Fashion",499,699,"👕","NEW",4.2));
        products.add(new Product(7,"Insulated Travel Bottle","Home",399,599,"🥤","VALUE PICK",4.6));
        products.add(new Product(8,"Premium Dry Fruit Mix","Grocery",549,699,"🥜","FRESH",4.8));
        products.add(new Product(9,"Portable Bluetooth Speaker","Electronics",999,1499,"🔊","DEAL",4.4));
        products.add(new Product(10,"Classic Everyday Backpack","Fashion",799,1099,"🎒","NEW",4.5));
        products.add(new Product(11,"Aroma Candle Gift Set","Home",449,599,"🕯️","GIFT PICK",4.3));
        products.add(new Product(12,"Daily Care Essentials Kit","Beauty",349,499,"🧼","VALUE PICK",4.1));
    }
    void restore(){
        try{
            JSONArray a=new JSONArray(prefs.getString("cart","[]"));for(int i=0;i<a.length();i++){JSONObject x=a.getJSONObject(i);cart.put(x.getInt("id"),x.getInt("qty"));}
            JSONArray w=new JSONArray(prefs.getString("wish","[]"));for(int i=0;i<w.length();i++)wishlist.add(w.getInt(i));
            JSONArray o=new JSONArray(prefs.getString("orders","[]"));for(int i=0;i<o.length();i++)orders.add(o.getString(i));
        }catch(Exception ignored){}
    }
    void save(){
        JSONArray a=new JSONArray();for(Integer id:cart.keySet()){JSONObject x=new JSONObject();try{x.put("id",id);x.put("qty",cart.get(id));a.put(x);}catch(Exception ignored){}}
        JSONArray w=new JSONArray();for(Integer id:wishlist)w.put(id);JSONArray o=new JSONArray();for(String s:orders)o.put(s);
        prefs.edit().putString("cart",a.toString()).putString("wish",w.toString()).putString("orders",o.toString()).apply();
    }
    int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    GradientDrawable bg(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.HORIZONTAL);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
    void pad(View v,int a){v.setPadding(dp(a),dp(a),dp(a),dp(a));}
    void space(LinearLayout l,int h){View v=new View(this);l.addView(v,new LinearLayout.LayoutParams(1,dp(h)));}
    TextView button(String s,Runnable action,boolean primary){TextView b=text(s,13,primary?Color.WHITE:INK,true);b.setGravity(Gravity.CENTER);pad(b,12);b.setBackground(bg(primary?ORANGE:Color.WHITE,10));if(!primary)b.setBackground(border(Color.WHITE,LINE,10));b.setOnClickListener(v->action.run());return b;}
    GradientDrawable border(int color,int stroke,int radius){GradientDrawable d=bg(color,radius);d.setStroke(dp(1),stroke);return d;}
    void base(String title,String subtitle){
        root=column();root.setBackgroundColor(BG);setContentView(root);
        LinearLayout top=row();top.setBackgroundColor(NAVY);pad(top,16);
        TextView logo=text("N",22,Color.WHITE,true);logo.setGravity(Gravity.CENTER);logo.setBackground(bg(ORANGE,12));top.addView(logo,new LinearLayout.LayoutParams(dp(42),dp(42)));
        LinearLayout brand=column();TextView name=text("Nisha Kart",19,Color.WHITE,true);TextView tagline=text("SHOP SMART. LIVE BETTER.",8,0xffc4ccda,true);brand.addView(name);brand.addView(tagline);LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(0,-2,1);bp.leftMargin=dp(10);top.addView(brand,bp);
        TextView cartBtn=text("🛒  "+cartCount(),14,Color.WHITE,true);cartBtn.setPadding(dp(12),dp(10),dp(12),dp(10));cartBtn.setBackground(bg(0xff263550,10));cartBtn.setOnClickListener(v->showCart());top.addView(cartBtn);root.addView(top);
        if(title.equals("Home")){
            search=new EditText(this);search.setSingleLine(true);search.setTextSize(13);search.setHint("Search products and brands");search.setPadding(dp(14),0,dp(14),0);search.setBackground(bg(Color.WHITE,10));LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,dp(46));sp.setMargins(dp(14),dp(12),dp(14),dp(8));root.addView(search,sp);
            search.setOnEditorActionListener((v,a,event)->{showHome();return true;});
        }
        ScrollView scroll=new ScrollView(this);content=column();content.setPadding(dp(14),dp(12),dp(14),dp(24));scroll.addView(content);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=row();nav.setBackgroundColor(Color.WHITE);nav.setElevation(dp(8));
        String[][] tabs={{"⌂","Home"},{"♡","Saved"},{"▤","Orders"},{"☰","Menu"}};
        for(String[] tab:tabs){LinearLayout item=column();item.setGravity(Gravity.CENTER);TextView icon=text(tab[0],19,ORANGE,true);TextView label=text(tab[1],10,INK,true);item.addView(icon);item.addView(label);item.setPadding(0,dp(8),0,dp(8));item.setOnClickListener(v->{if(tab[1].equals("Home"))showHome();else if(tab[1].equals("Saved"))showWishlist();else if(tab[1].equals("Orders"))showOrders();else showMenu();});nav.addView(item,new LinearLayout.LayoutParams(0,-2,1));}
        root.addView(nav);
    }
    int cartCount(){int n=0;for(int q:cart.values())n+=q;return n;}
    String money(int n){return "₹"+String.format(Locale.US,"%,d",n);}
    void heading(String title,String subtitle){TextView h=text(title,22,INK,true);content.addView(h);if(subtitle!=null){TextView s=text(subtitle,12,MUTED,false);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(4);content.addView(s,p);}space(content,14);}
    void showHome(){
        base("Home",null);
        LinearLayout hero=column();hero.setPadding(dp(20),dp(22),dp(20),dp(22));hero.setBackground(bg(NAVY,18));
        hero.addView(text("THE NISHA KART EDIT",10,0xffffa47f,true));space(hero,9);
        TextView title=text("Everything you love.\nAll in one cart.",27,Color.WHITE,true);hero.addView(title);space(hero,8);
        hero.addView(text("Discover fresh finds across fashion, tech, home and more.",12,0xffc5cede,false));space(hero,15);
        hero.addView(button("Explore products  →",()->{category="All";showHome();},true));content.addView(hero);space(content,20);
        heading("Shop by category","Find what you need, your way");
        String[][] cats={{"🎧","Electronics"},{"👟","Fashion"},{"🏡","Home"},{"🧴","Beauty"},{"🥜","Grocery"}};
        for(int i=0;i<cats.length;i+=2){LinearLayout r=row();for(int j=i;j<Math.min(i+2,cats.length);j++){final String c=cats[j][1];TextView b=text(cats[j][0]+"  "+c,13,INK,true);pad(b,15);b.setBackground(border(Color.WHITE,LINE,12));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(58),1);p.setMargins(0,0,dp(8),0);b.setOnClickListener(v->{category=c;showHome();});r.addView(b,p);}content.addView(r);space(content,8);}
        LinearLayout titleRow=row();titleRow.addView(text(category.equals("All")?"Popular right now":category+" picks",21,INK,true),new LinearLayout.LayoutParams(0,-2,1));titleRow.addView(button("All items",()->{category="All";filter="All";showHome();},false));content.addView(titleRow);space(content,10);
        String q=search==null?"":search.getText().toString().trim().toLowerCase(Locale.ROOT);
        ArrayList<Product> shown=new ArrayList<>();for(Product p:products){if(!category.equals("All")&&!p.category.equals(category))continue;if(filter.equals("Deals")&&!p.tag.equals("DEAL"))continue;if(!q.isEmpty()&&!p.name.toLowerCase(Locale.ROOT).contains(q)&&!p.category.toLowerCase(Locale.ROOT).contains(q))continue;shown.add(p);}
        if(shown.isEmpty())content.addView(text("No products found. Try another search.",13,MUTED,false));
        for(Product p:shown)productCard(p);
    }
    void productCard(Product p){
        LinearLayout card=column();card.setPadding(dp(14),dp(12),dp(14),dp(12));card.setBackground(border(Color.WHITE,LINE,14));LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.bottomMargin=dp(10);content.addView(card,cp);
        LinearLayout r=row();TextView emoji=text(p.emoji,42,INK,false);emoji.setGravity(Gravity.CENTER);emoji.setBackground(bg(BG,12));r.addView(emoji,new LinearLayout.LayoutParams(dp(78),dp(78)));
        LinearLayout info=column();LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(0,-2,1);ip.leftMargin=dp(12);r.addView(info,ip);
        info.addView(text(p.tag+"  ·  "+p.category.toUpperCase(Locale.ROOT),9,ORANGE,true));space(info,4);info.addView(text(p.name,14,INK,true));space(info,4);info.addView(text("★ "+p.rating+"   ·   "+money(p.price),12,MUTED,true));space(info,4);info.addView(text(money(p.old)+"  (special price)",10,MUTED,false));
        card.addView(r);space(card,10);LinearLayout actions=row();TextView wish=button(wishlist.contains(p.id)?"♥ Saved":"♡ Save",()->{if(wishlist.contains(p.id))wishlist.remove(p.id);else wishlist.add(p.id);save();showHome();},false);actions.addView(wish,new LinearLayout.LayoutParams(0,-2,1));TextView add=button("＋ Add to cart",()->{cart.put(p.id,cart.getOrDefault(p.id,0)+1);save();toast("Added to cart");showHome();},true);LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(0,-2,1);ap.leftMargin=dp(8);actions.addView(add,ap);card.addView(actions);
    }
    void showCart(){
        base("Cart",null);heading("Your cart",cartCount()+" item(s) selected");int total=0;
        if(cart.isEmpty()){content.addView(text("🛒",42,ORANGE,true));content.addView(text("Your cart is waiting",18,INK,true));content.addView(text("Add something you love and it will show up here.",12,MUTED,false));return;}
        for(Product p:products)if(cart.containsKey(p.id)){int qty=cart.get(p.id);total+=p.price*qty;LinearLayout r=row();TextView t=text(p.emoji+"  "+p.name+"\n"+money(p.price)+" × "+qty,13,INK,true);r.addView(t,new LinearLayout.LayoutParams(0,-2,1));r.addView(button("−",()->{int n=cart.get(p.id)-1;if(n<=0)cart.remove(p.id);else cart.put(p.id,n);save();showCart();},false));r.addView(button("＋",()->{cart.put(p.id,cart.get(p.id)+1);save();showCart();},false));content.addView(r);space(content,12);}
        final int amount=total;space(content,12);content.addView(text("Total: "+money(total),22,INK,true));space(content,12);content.addView(button("Continue to checkout →",()->checkout(amount),true));
    }
    void checkout(int amount){
        LinearLayout form=column();EditText name=field(form,"Full name");EditText phone=field(form,"Mobile number");phone.setInputType(InputType.TYPE_CLASS_PHONE);EditText address=field(form,"Delivery address");address.setMinLines(3);address.setGravity(Gravity.TOP);
        new AlertDialog.Builder(this).setTitle("Delivery details").setView(form).setNegativeButton("Cancel",null).setPositiveButton("Place demo order",(d,w)->{
            if(name.getText().toString().trim().isEmpty()||phone.getText().toString().trim().length()<10||address.getText().toString().trim().isEmpty()){toast("Please enter valid delivery details");return;}
            String order="#"+(System.currentTimeMillis()%100000000)+" · "+name.getText()+" · "+money(amount)+" · "+phone.getText()+" · "+address.getText();orders.add(order);cart.clear();save();new AlertDialog.Builder(this).setTitle("Demo order placed!").setMessage("Your order is saved on this device. No payment or delivery is connected yet.").setPositiveButton("View orders",(x,y)->showOrders()).show();
        }).show();
    }
    EditText field(LinearLayout box,String hint){EditText e=new EditText(this);e.setHint(hint);e.setTextSize(14);e.setPadding(dp(12),dp(8),dp(12),dp(8));e.setBackground(border(Color.WHITE,LINE,9));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(dp(12),dp(6),dp(12),dp(6));box.addView(e,p);return e;}
    void showWishlist(){base("Saved",null);heading("Your wishlist","Products you saved");boolean any=false;for(Product p:products)if(wishlist.contains(p.id)){any=true;productCard(p);}if(!any)content.addView(text("No saved products yet.",14,MUTED,false));}
    void showOrders(){base("Orders",null);heading("Your orders","Your recent shopping activity");if(orders.isEmpty()){content.addView(text("No orders yet. Place a demo order to see it here.",13,MUTED,false));return;}for(int i=orders.size()-1;i>=0;i--){TextView t=text(orders.get(i)+"\nDEMO ORDER · Saved on this device",13,INK,true);pad(t,14);t.setBackground(border(Color.WHITE,LINE,12));content.addView(t);space(content,9);}}
    void showMenu(){base("Menu",null);heading("More from Nisha Kart","Shopping made simple");content.addView(button("♡  Saved products",()->showWishlist(),false));space(content,8);content.addView(button("▤  Your orders",()->showOrders(),false));space(content,8);content.addView(button("⚙  Store admin demo",()->showAdmin(),false));space(content,8);content.addView(button("ⓘ  About Nisha Kart",()->new AlertDialog.Builder(this).setTitle("Nisha Kart").setMessage("An independent shopping app prototype. No Firebase, payment gateway, shipping or online account backend is connected.").setPositiveButton("OK",null).show(),false));}
    void showAdmin(){base("Admin",null);heading("Store dashboard","Local demo controls");content.addView(text(products.size()+" products     ·     "+orders.size()+" demo orders",16,INK,true));space(content,14);content.addView(button("＋ Add product",()->addProduct(),true));space(content,9);content.addView(button("View demo orders",()->showOrders(),false));space(content,9);content.addView(button("Reset demo data",()->new AlertDialog.Builder(this).setTitle("Reset app data?").setMessage("This clears cart, wishlist and demo orders.").setNegativeButton("Cancel",null).setPositiveButton("Reset",(d,w)->{cart.clear();wishlist.clear();orders.clear();save();showAdmin();}).show(),false));space(content,14);content.addView(text("Admin demo is local and has no secure login. Do not use for live store operations.",11,MUTED,false));}
    void addProduct(){LinearLayout form=column();EditText name=field(form,"Product name");EditText price=field(form,"Price in rupees");price.setInputType(InputType.TYPE_CLASS_NUMBER);EditText emoji=field(form,"Product emoji (e.g. 📦)");Spinner cat=new Spinner(this);String[] opts={"Electronics","Fashion","Home","Beauty","Grocery"};cat.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,opts));form.addView(cat);new AlertDialog.Builder(this).setTitle("Add product").setView(form).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{try{int val=Integer.parseInt(price.getText().toString());products.add(new Product(nextId++,name.getText().toString(),cat.getSelectedItem().toString(),val,val,emoji.getText().toString().isEmpty()?"📦":emoji.getText().toString(),"NEW",4.2));showAdmin();}catch(Exception ex){toast("Enter a valid product name and price");}}).show();}
    void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
}
