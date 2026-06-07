import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.minimalhealth.app',
  appName: 'MinimalHealth',
  webDir: 'dist',
  server: {
    androidScheme: 'http'
  }
};

export default config;
