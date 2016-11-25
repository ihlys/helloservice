package com.ihordev.helloservice.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Contacts")
public class Contact implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @SortAttribute
    private String name;

    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if ((obj instanceof Contact) == false)
            return false;
        
        Contact other = (Contact) obj;
        
        if (getId() == null)
        {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        
        if (getName() == null)
        {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        
        return true;
    }

    @Override
    public String toString()
    {
        return "Contact [id=" + id + ", name=" + name + "]";
    }

}
