package modlan.report;

/**
 * Created by midvorki on 3/15/14.
 */
public enum Severity
{
    TODO("TODO:", false),
    INFO("INFO:", false),
    WARN("WARN:", false),
    ERROR("ERR:", false),
    DEATH("DEATH:", true)
    ;

    Severity(String aInName, boolean aInAbort)
    {
        name = aInName;
        abort = aInAbort;
    }

    public void report(String aInContext, String aInAction, String aInCause, String aInM)
    {
        report(aInContext, aInAction, aInCause, aInM, null);
    }

    public void report(String aInContext, String aInAction, String aInCause, String aInM, Throwable aInT)
    {
        StringBuffer lSb = new StringBuffer();
        format(
              lSb,
              aInContext,
              aInAction,
              aInCause,
              aInM,
              null
              );

        if (abort)
        {
            if (null != aInT)
            {
                aInT.printStackTrace();
            }
            else
            {
                new Error(lSb.toString()).printStackTrace();
            }
            System.exit(666);
        }
        else
        {
            if (null != aInT)
            {
                aInT.printStackTrace();
            }
            System.out.println(lSb.toString());
        }
    }

    public void report(String aInContext, String aInAction, String aInCause, Throwable aInT)
    {
        report(aInContext, aInAction, aInCause, null, aInT);
    }


    public void stack(String aInContext, String aInAction, String aInCause, String aInM)
    {
        stack(aInContext, aInAction, aInCause, aInM, null);
    }

    public void stack(String aInContext, String aInAction, String aInCause, String aInM, Throwable aInT)
    {
        StringBuffer lSb = new StringBuffer();
        format(
                      lSb,
                      aInContext,
                      aInAction,
                      aInCause,
                      aInM,
                      null
              );

        if (null != aInT)
        {
            aInT.printStackTrace();
            new Exception(lSb.toString(), aInT).printStackTrace();
        }
        else
        {
            new Exception(lSb.toString()).printStackTrace();
        }
    }

    public void stack(String aInContext, String aInAction, String aInCause, Throwable aInT)
    {
        stack(aInContext, aInAction, aInCause, null, aInT);
    }

    private void format(
            StringBuffer aOutSb,
            String aInContext,
            String aInAction,
            String aInCause,
            String aInM,
            Throwable aInT
            )
    {
        int lThisCnt;
        synchronized (this) { lThisCnt = count++; };

        aOutSb.append(lThisCnt);
        aOutSb.append('>');
        aOutSb.append(name);
        if (null != aInContext && 0 < aInContext.length())
        {
            aOutSb.append(" CTX=[");
            aOutSb.append(aInContext);
            aOutSb.append(']');
        }
        if (null != aInAction && 0 < aInAction.length())
        {
            aOutSb.append(" ACT=[");
            aOutSb.append(aInAction);
            aOutSb.append(']');
        }
        if (null != aInCause && 0 < aInCause.length())
        {
            aOutSb.append(" CAUSE=[");
            aOutSb.append(aInCause);
            aOutSb.append(']');
        }
        if (null != aInM && 0 < aInM.length())
        {
            aOutSb.append(" MSG=[");
            aOutSb.append(aInM);
            aOutSb.append(']');
        }
        if (null != aInT)
        {
            aOutSb.append(" EXC=[");
            aOutSb.append(aInT.toString());
            if (null != aInT.getMessage() && 0 < aInT.getMessage().length())
            {
                if (null != aInM && 0 < aInM.length())
                {
                    aOutSb.append(" | ");
                }
                aOutSb.append(aInT.getMessage());
            }
            aOutSb.append(']');
        }
    }

    public String getName()
    {
        return name;
    }

    public boolean isAbort()
    {
        return abort;
    }

    private String name;
    private boolean abort;
    private static int count = 0;
}
