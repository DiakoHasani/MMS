package ir.tdaapp.mms.Model.Utilitys;

//در اینجا برای جلوگیری از درست شدن کلاس های اضافه از این کلاس استفاده می کنیم
public class ETC {

    //در اینجا رپلیس های مربوط به اسپیچ تو تکست انجام می شود
    public static String ReplaceSpechToText(String text) {

        String res = text;

        res = res.replace("نقطه سر خط", "\n");
        res = res.replace("خط بعدی", "\n");
        res = res.replace("اینتر", "\n");
        res = res.replace("انتر", "\n");
        res = res.replace("نقطه", ".");
        res = res.replace("ویرگول", "،");
        res = res.replace("کاما", ",");
        res = res.replace("هشتگ", "#");
        res = res.replace("هشتک", "#");
        res = res.replace("صحفه", "صفحه");
        res = res.replace("علامت سوال", "?");
        res = res.replace("اتساین", "@");
        res = res.replace("درصد", "%");
        res = res.replace("ستاره", "*");
        res = res.replace("خط تیره", "-");
        res = res.replace("پرانتز باز", "(");
        res = res.replace("پرانتز بسته", ")");

        return res;
    }

}
