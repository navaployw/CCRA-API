
import static com.arg.ccra3.common.FileManagerUtil.createDirIfNotExist;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class FileManagerUtilTest {
    
    @Test
    public void test_mkdir(){
        File dir = new File("/share/fileattachment/stored/htmlReport/asdf11111111/qwer/gfhj/iuyo");
        assertTrue(dir.mkdirs());
    }
    
    @Test
    public void test_createDirIfNotExist(){
        File dir = createDirIfNotExist("/share/fileattachment/stored/htmlReport/asdf/qwer/gfhj/iuyo");
        System.out.println(dir.getAbsoluteFile());
        assertNotNull(dir);
    }
}
