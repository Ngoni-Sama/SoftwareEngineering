package domain;

/**
 * Created by Alex on 4/25/2015.
 */
public class StandardAddition extends SolutionSet {

    Solution internalStandard, analyte;
    private double standardVol = 0.0;
    private double standardVolT = 0.0;
    private double standardMolarity = 0.0;
    private double analyteVolT = 0.0;
    private double analyteMolarity = 0.0;

    public StandardAddition(Solution analyte, Solution internalStandard){
        super("Internal Standards");

        this.analyte = analyte;
        this.internalStandard = internalStandard;

        String[] questions = super.concat(analyte.getQUESTIONS(), internalStandard.getQUESTIONS());
        questions = super.concat(questions, new String[]{
                "What is the volume of the new standard? (in mL)",
                "What is the volume of the internal standard that you are transferring into the new standard? (in mL)",
                "What is the molarity of the internal standard in the new standard? (round to the 2nd Decimal)",
                "What is the volume of the stock analyte that you are transferring into the new standard? (in mL)",
                "What is the molarity of the stock analyte in the new standard? (round to the 2nd Decimal)"
        } );

        Answer[] answers = super.concat(analyte.getANSWERS(), internalStandard.getANSWERS());
        answers = super.concat(answers, new Answer[]{
                new Answer("double", false),
                new Answer("double", true, true),
                new Answer("double", true),
                new Answer("double", true, true),
                new Answer("double", true)
        });

        super.setQUESTIONS(questions);
        super.setANSWERS(answers);
    }

    @Override
    public void compute(int count) {
        if(count == 5) {
            analyte.setANSWERS(getANSWERS());
            analyte.compute(count);
        }
        if(count == 11){
            internalStandard.setANSWERS(getANSWERS());
            internalStandard.compute(count);
        }
        if(count == 14) {
            calcStandardMolarity(internalStandard.getSolMolarity(), standardVolT, standardVol);
        }
        else {
            calcAnalyteMolarity(analyte.getSolMolarity(), analyteVolT, standardVol);

            Solution newSolution = new Solution("Internal Standard", standardVolT, internalStandard.getSolvent(), internalStandard.getSolute(), internalStandard.getSoluteMolWeight(), standardMolarity);
            newSolution.compute(count);
            setDETAILS(newSolution.getDETAILS());
            setDATA(newSolution.getDATA());
        }
    }

    public double getCompare(int count){
        if(count == 5)
            return analyte.getCompare(count);
        else if(count == 11)
            return internalStandard.getCompare(count);
        else if(count == 13)
            return internalStandard.getCompare2();
        else if(count == 14)
            return standardMolarity;
        else if(count == 17)
            return analyte.getCompare2();
        return analyteMolarity;
    }

    public double getCompare2() {
        return standardVol;
    }

    public String getDialog() {
        return "Would you like to create another standard?";
    }

    public int getRestart() {
        return 18;
    }

    public void setValues(Answer[] answers, int count) {
        analyte.setValues(answers, count);
        setAnsw(analyte.getAnsw());
        if(count <= 11) {
            internalStandard.setValues(answers, count);
            setAnsw(internalStandard.getAnsw());
        }
        else if (count <= 14) {
            for(int i = 14; i <= count; i++) {
                switch(i) {
                    case 12:
                        setStandardVol(Double.parseDouble(answers[i].getVALUE()) / 1000);
                        break;
                    case 13:
                        setStandardVolT(Double.parseDouble(answers[i].getVALUE()) / 1000);
                        setAnsw(Double.parseDouble(answers[i].getVALUE()) / 1000);
                        break;
                    case 14:
                        setAnsw(Double.parseDouble(answers[i].getVALUE()) / 1000);
                        break;
                }
            }
        }
        else {
            for (int i = 9; i <= count; i++) {
                switch (i) {
                    case 17:
                        setAnalyteVolT(Double.parseDouble(answers[i].getVALUE()) / 1000);
                        setAnsw(Double.parseDouble(answers[i].getVALUE()) / 1000);
                        break;
                    case 18:
                        setAnsw(Double.parseDouble(answers[i].getVALUE()) / 1000);
                        break;
                }
            }
        }
    }

    public void calcStandardMolarity(double solutionMolarity, double volTran, double vol) {
        standardMolarity = solutionMolarity * (volTran / vol);
    }
    public void calcAnalyteMolarity(double solutionMolarity, double volTran, double vol) {
        analyteMolarity = solutionMolarity * (volTran/vol);
    }

    public double getStandardVol() {
        return standardVol;
    }

    public void setStandardVol(double standardVol) {
        this.standardVol = standardVol;
    }

    public double getStandardVolT() {
        return standardVolT;
    }

    public void setStandardVolT(double standardVolT) {
        this.standardVolT = standardVolT;
    }

    public double getStandardMolarity() {
        return standardMolarity;
    }

    public void setStandardMolarity(double standardMolarity) {
        this.standardMolarity = standardMolarity;
    }

    public double getAnalyteVolT() {
        return analyteVolT;
    }

    public void setAnalyteVolT(double analyteVolT) {
        this.analyteVolT = analyteVolT;
    }

    public double getAnalyteMolarity() {
        return analyteMolarity;
    }

    public void setAnalyteMolarity(double analyteMolarity) {
        this.analyteMolarity = analyteMolarity;
    }
}