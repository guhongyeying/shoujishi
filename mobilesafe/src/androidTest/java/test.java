import android.content.Context;
import android.test.AndroidTestCase;

import com.example.mobilesafe.db.dao.BlachNumberDao;

import java.util.Random;

/**
 * Created by wangren on 15/8/27.
 */
public class test  extends AndroidTestCase{


    public Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mContext = getContext();
    }

    public void testInsert() {
        BlachNumberDao blachNumberDao = new BlachNumberDao(mContext);

        Random random = new Random();
        for (int i = 0; i <= 57; i++) {
            String number = String.valueOf(i);
            String mode = String.valueOf(random.nextInt(3)+1);
            blachNumberDao.addContacts(number,mode);
        }
    }
}
