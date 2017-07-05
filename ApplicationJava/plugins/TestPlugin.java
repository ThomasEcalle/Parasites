/**
 * Created by Thomas Ecalle on 02/07/2017.
 */
public class TestPlugin implements PluginInterface
{
    int parameter;

    @Override
    public void setParameter(int parameter)
    {
        this.parameter = parameter;
    }

    @Override
    public int getResult()
    {
        return parameter * 2;
    }

    @Override
    public String getPluginName()
    {
        return "thomas plugin";
    }

    @Override
    public boolean hasError()
    {
        return false;
    }
}
