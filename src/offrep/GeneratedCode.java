
package offrep;


   
public class GeneratedCode {
        public String Result;
        
    public GeneratedCode() {
        
    }
    public String generation(){
        String code;
        String result = makePassword(8);
        Result = result;
        code = result;
        return code;
    }
    public static String makePassword(int length){
        String password = "";
        
        for(int i = 0; i < length-2;i++){
            password = password + randomCharacter("123456789");
        }
        String randomDigit = randomCharacter("ABCDEGHJKLMNPQRTUVWXYZ");
        password = insertAtRandom(password,randomDigit);
     
        return password;
    }
    public static String randomCharacter(String characters){
        int n = characters.length();
        int r = (int) (n * Math.random());
        return characters.substring(r,r+1);
    }
    public static String insertAtRandom(String str,String toInsert){
        int n = str.length();
        int r = (int)((n + 1) * Math.random());
        return str.substring(0, r) + toInsert + str.substring(r);
    }
}
