package com.example.glutenfree;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;

public class check_Input {
    private String isnot = "+*%@$!#^()_={}[]/|&`~:.;'" + '"' + " ";
    private Context context;
    private TextInputEditText inputEditText;
    private String text;

    public check_Input(Context context) {
        this.context = context;
    }

    public void setInputEditText(TextInputEditText inputEditText) {
        this.inputEditText = inputEditText;
    }

    public void setInputText(String text) {
        this.text = text;
    }

    public boolean checkName(String name) {
        boolean f = true;
        try {
            int i, j;
            boolean f1 = true, f2 = true;
            if (name.length() == 0) {
                f = false;
                throw new Exception("יש להזין שם");
            }
            for (i = 0; i < name.length(); i++) {
                if (name.charAt(i) >= '0' && name.charAt(i) <= '9') {
                    f1 = false;
                    f = false;
                }
            }
            if (!f1)
                throw new Exception("אין להזין מספרים");
            for (i = 0; i < name.length(); i++) {
                for (j = 0; j < this.isnot.length(); j++) {
                    if (name.charAt(i) == this.isnot.charAt(j)) {
                        f2 = false;
                        f = false;
                    }
                }
                if (!f2)
                    throw new Exception("אין להזין תווים מיוחדים");

            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());

            return f;
        }
        return f;
    }


    public boolean checkPass(String pass) {
        boolean f = true;
        int i, j;
        try {
            if (pass.length() == 0) {
                f = false;
                throw new Exception("חובה למלא סיסמה");
            }
            if (pass.length() < 4) {
                f = false;
                throw new Exception("סיסמה חייבת להכיל לפחות 4 תווים");
            }
            for (i = 0; i < pass.length(); i++) {
                if (pass.toString().charAt(i) == ' ')
                    throw new Exception("סיסמה אינה יכולה להכיל רווחים");

            }

        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }
        return f;
    }

    public boolean checkMail(String mail) {
        boolean f = true;
        int i, j, counter;
        try {
            if (mail.length() == 0) {
                f = false;
                throw new Exception("חובה למלא אימייל");
            }
            String[] arr = mail.split("@");
            if (arr.length == 1) {
                f = false;
                throw new Exception("אימייל חייב להכיל את התו @");
            }
            if (arr.length > 2) {
                f = false;
                throw new Exception("אימייל חייב להכיל @ אחד בלבד");
            }
            String[] vec1 = arr[0].split(".");
            if (vec1.length > 2) {
                f = false;
                throw new Exception("אסור יותר מנקודה אחת בחלק שלפני ה@");
            }
            for (i = 0; i < vec1.length; i++)
                if (vec1[i].length() < 2) {
                    f = false;
                    throw new Exception("חובה לשים לפחות 2 תווים בין הנקודות באימייל");
                }
            String[] vec2 = arr[1].split(".");
            if (vec2.length == 1) {
                f = false;
                throw new Exception("חובה לפחות נקודה אחת בחלק שאחרי ה @");

            }
            if (vec2.length > 3) {
                f = false;
                throw new Exception("אסור יותר משתי נקודות בחלק שאחרי ה@");


            }
            for (i = 0; i < vec2.length; i++)
                if (vec2[i].length() < 2) {
                    f = false;
                    throw new Exception("חובה לשים לפחות 2 תווים בין הנקודות באימייל");


                }
            if (vec2.length == 2 && vec2[1].length() != 3) {
                f = false;
                throw new Exception("חובה 3 תווים אחרי הנקודה האחרונה");
            }
            if (vec2.length == 3 && vec2[2].length() != 2) {
                f = false;
                throw new Exception("חובה 2 תווים בדיוק אחרי הנקודה האחרונה");
            }
            for (i = 0; i < mail.length(); i++) {
                if (mail.toString().charAt(i) == ' ')
                    throw new Exception("מייל אינו יכול להכיל רווחים");
            }

        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }

        return f;
    }

    public boolean checkBusinessName(String businessName) {
        boolean f = true;
        try {
            if (businessName.length() == 0) {
                f = false;
                throw new Exception("יש למלא את שם בית העסק");
            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }
        return f;
    }

    public boolean checkBusinessDescription(String businessDescription) {
        boolean f = true;
        try {
            if (businessDescription.length() == 0) {
                f = false;
                throw new Exception("יש למלא את תיאור פרטי העסק");
            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }
        return f;
    }

    public boolean checkBusinessAddress(String businessAddress) {
        boolean f = true;
        try {
            if (businessAddress.length() == 0) {
                f = false;
                throw new Exception("יש להזין את כתובת בית העסק");
            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }
        return f;
    }

    public boolean checkBusinessCityAddress(String businessAddress) {
        boolean f = true;
        try {
            if (businessAddress.length() == 0) {
                f = false;
                throw new Exception("יש להזין את העיר בו נמאצ העסק, או הערים בו נמצאים סניפי העסק");
            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }
        return f;
    }

    public boolean checkOpeningHours(String businessOpeningHours) {
        boolean f = true;
        try {
            if (businessOpeningHours.length() == 0) {
                f = false;
                throw new Exception("יש להזין את שעות הפעילות של העסק");
            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }
        return f;
    }

    public boolean checkDeliveryDetails(String deliveryDetails) {
        boolean f = true;
        try {
            if (deliveryDetails.length() == 0) {
                f = false;
                throw new Exception("יש להזין את דרכי המשלוח");
            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());
            return f;
        }
        return f;
    }

    public boolean checkPhone(String phone) {
        boolean f = true;
        int i, j;
        try {
            if (phone.length() == 0) {
                f = false;
                throw new Exception(" חובה למלא מספר טלפון");
            }

            if (phone.length() != 10) {
                f = false;
                throw new Exception(" מס' טלפון חייב להכיל 10 ספרות");


            }
            for (i = 0; i < phone.length(); i++) {
                if (!(phone.charAt(i) >= '0' &&
                        phone.charAt(i) <= '9'&&  phone.charAt(i)== '-')) {

                    f = false;
                    throw new Exception("מס' טלפון חייב להכיל ספרות בלבד");
                }
            }
            if ((!phone.startsWith("050")) && (!phone.startsWith("052")) && (!phone.startsWith("053")) && (!phone.startsWith("054")) && (!phone.startsWith("055")) && (!phone.startsWith("057")) && (!phone.startsWith("058")) && (!phone.startsWith("077")) && (!phone.startsWith("08") && (!phone.startsWith("03")))) {
                f = false;
                throw new Exception("הקידומת אינה תקינה");
            }
        } catch (Exception e) {
            inputEditText.setError(e.getMessage());

            return f;
        }
        return f;
    }


    @Override
    public String toString() {
        return "check_Input{}";
    }

}