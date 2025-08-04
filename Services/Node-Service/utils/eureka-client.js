const { Eureka } = require('eureka-js-client');

const client = new Eureka({
  instance: {
    app: 'NODE-SERVICE',
    instanceId: 'node-service-1',
    hostName: 'localhost',
    ipAddr: '127.0.0.1',
    statusPageUrl: 'http://localhost:4000/info',
    port: {
      '$': 4000,
      '@enabled': true,
    },
    vipAddress: 'node-service',
    dataCenterInfo: {
      name: 'MyOwn',
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo'
    }
  },
  eureka: {
    host: 'localhost',
    port: 8761,
    servicePath: '/eureka/apps/'
  }
});

// Start the registration process
client.start(error => {
  if (error) {
    console.error('Eureka registration failed:', error);
  } else {
    console.log('Node service registered with Eureka');
  }
});
