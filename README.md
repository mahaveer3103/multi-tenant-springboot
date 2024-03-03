# Multi-Tenant Database Configuration in Spring Boot

Multi-tenancy is an architecture in which a single instance of a software application serves multiple customers. Each customer is called a tenant. In this repository, we have implemented a shared database with separate schemas, meaning all tenants share a database but have their own database schemas and tables.

## How it Works

1. **Database Configuration Files**: We have provided two database configuration files within the `resource` folder with `dbconfig` extentions. One with having `default` name and other with `local2` in there files. Each configuration file is named after the respective tenant. These configuration files specify the datasource details for each tenant.

2. **Tenant Path Configuration**: The paths to these database configuration files are specified in the `application.properties` file. This allows the application to scan and load the configurations dynamically.

3. **MultiTenantConfiguration**: This class is responsible for creating datasources for each tenant based on the configuration files provided. It scans the specified paths and extracts datasource configurations, storing them in a map.

4. **TenantFilter**: During runtime, when a user makes a request with an `orgId` header, the `TenantFilter` intercepts the request. It uses the `orgId` to filter and retrieve the corresponding datasource from the map created by `MultiTenantConfiguration`. This sets the current tenant's database configurations for all operations related to that request.

## Usage

1. **Configure Database Properties**: Add database configuration files for each tenant in the `resource` folder. Ensure each file is named after the respective tenant.

2. **Specify Configuration Paths**: Update the `application.properties` file to include the paths to the database configuration files.

3. **Run Application**: Execute the Spring Boot application. The application will dynamically load the datasource configurations for each tenant upon startup.

4. **API Usage**: When making API requests, include the `orgId` header to specify the desired tenant. The `TenantFilter` will handle the routing of requests to the appropriate database based on the provided `orgId`.

## Example

Suppose we have two tenants, Tenant A and Tenant B, each with their own database configurations in resources folder.

1. **Database Configuration Files**:
    - `abc.dbconfig`
      
   ```
      name=default
      datasource.url=jdbc:mysql://localhost:3306/db_1
      datasource.username=root
      datasource.password=root
      datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```
   
    - `abc2.dbconfig`

    ```
      name=tenantB
      datasource.url=jdbc:mysql://localhost:3306/db_1
      datasource.username=root
      datasource.password=root
      datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    ```

2. **application.properties**:
    ```properties
    multi-tenant.datasource.paths=classpath:/resources
    ```

3. **Request Header**:
    ```
    orgId: tenantB
    ```

With the above setup, when a request is made with `orgId: tenantB`, the application will use the database configuration specified in `abc2.dbconfig`. But if not send any `orgId`then it will take the `default` configurations.

## Below video shows the structure of the project with the basic understanding of how it actually works :

https://github.com/mahaveer3103/multi-tenant-springboot/assets/112773191/04f226d5-077f-4b49-b0ac-ee0da4e572b4


This setup enables seamless multi-tenancy support within the Spring Boot application, allowing different tenants to interact with their respective databases securely and efficiently.

Feel free to explore and contribute to this repository for further enhancements and customization options.
