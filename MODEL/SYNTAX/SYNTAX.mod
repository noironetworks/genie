

module<name>
{
    primitive<name,hint>
    {
        language<C>
        {

        }

        const<name,value>
        {
            auto-trans: "name"
        }

        const<name,value>
        {
            remove
        }

        validate<name>
        {
            range<name, min, max>
            content<name>:"regex"
            size<name,min,max>
        }
    }

    typedef<name,supertype>
    {
        const<name,value>
        {
            auto-trans: "name"
        }

        const<name,value>
        {
            remove|selectable|
        }

        validate<name>
        {
            range<name, min, max>
            content<name>:"regex"
            size<name,min,max>
        }
    }

    class<name,abstract|concrete:superclass>
    {
        member<name,type:type>
        {
            const<name,value>
            {
                auto-trans: "name"
            }

            const<name,value>
            {
                remove|selectable|
            }

            validate<name>
            {
                range<name, min, max>
                content<name>:"regex"
                size<name,min,max>
            }
        }

        override<name>
        {

        }
    }


}
